package Core;

import Core.modules.Freamon;
import util.Config;
import util.Schedule;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Created by joh-mue on 01/02/16.
 *
 */
public class Yarn {
    private Schedule schedule;

    public Yarn(Schedule schedule) {
        this.schedule = schedule;
    }

    public void initiateJobExecution() {
        for (JobSequence jobSequence: schedule) {
            Thread jobThread = new Thread(new JobRunner(jobSequence));
            jobThread.start();

            if (Config.getInstance().sequentialExecution()) {
                synchronized (jobThread) {
                    try {
                        jobThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class JobRunner implements Runnable {
        private JobSequence jobSequence;

        public JobRunner(JobSequence jobSequence) {
            this.jobSequence = jobSequence;
        }

        public void run() {
            synchronized (this) {
                for (Job job: jobSequence) {
                    executeJob(job);
                }
            }
        }

        private void executeJob(Job job) {
            try {
                Config config = Config.getInstance();

                System.out.println("Waiting " + job.getDelay() + " to execute " + job.getJobName());
                Thread.sleep(job.getDelay() * 1000, 0);

                PrintStream out = job.getLogPrintStream(Config.getLogDir(job.getExperimentName()));

                String[] envp = { "HADOOP_CONF_DIR=" + config.getHadoopConfDir() };

                System.out.println("Executing " + job + '+' + job.getDelay() + "sec with command: " + job.getCommand());
                InputStream inputStream = Runtime.getRuntime().exec(job.getCommand(),envp).getInputStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                long startTime = 0;
                while ((line = buff.readLine()) != null) {
                    out.println(line);
                    if (line.contains("Submitted application")) {
                        String jobID = line.substring(line.indexOf("Submitted application")).replace("Submitted application", "").trim();
                        job.setJobID(jobID);
                        if (config.notifyFreamon()) {
                            Freamon.onSubmit(job.getJobID());
                        }
                    }

                    if (line.contains("Job execution switched to status RUNNING.")) {
                        startTime = System.nanoTime();
                        if (config.notifyFreamon()) {
                            Freamon.onStart(job.getJobID(), startTime);
                        }
                    }

                    if (line.contains("Job execution switched to status FINISHED")) {
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime);
                        System.out.println("Executing " + job + '+' + job.getDelay() + "sec took " + duration / 1000000000 + " seconds to complete.");
                        if (config.notifyFreamon()) {
                            Freamon.onStop(job.getJobID(), endTime);
                        }
                    }
                }
                // TODO: create experiment summary file/output
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}