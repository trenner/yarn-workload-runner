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
        }
    }

    private class JobRunner implements Runnable {
        private JobSequence jobSequence;
        private String[] envp;
        private Config config;

        public JobRunner(JobSequence jobSequence) {
            this.jobSequence = jobSequence;
            config = Config.getInstance();
            envp = new String[] { "HADOOP_CONF_DIR=" + config.getHadoopConfDir() };
        }

        public void run() {
                for (Job job: jobSequence) {
                    executeJob(job);
                }
        }

        private void executeJob(Job job) {
            try {
                System.out.println("Waiting " + job.getDelay() + " to execute " + job.getJobName());
                Thread.sleep(job.getDelay() * 1000, 0);

                PrintStream logPrintStream = job.getLogPrintStream(Config.getLogDir(job.getExperimentName()));

                System.out.println("Executing " + job + '+' + job.getDelay() + "sec with command: " + job.getCommand());
                Process process = Runtime.getRuntime().exec(job.getCommand(),envp);
                InputStream inputStream = process.getInputStream();
                InputStream errorStream = process.getErrorStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream));
                BufferedReader buffErr = new BufferedReader(new InputStreamReader(errorStream));

                String line;
                long startTime = 0;
                while ((line = buff.readLine()) != null) {
                    logPrintStream.println(line);
                    if (line.contains("Submitted application")) {
                        String jobID = line.substring(line.indexOf("Submitted application")).replace("Submitted application", "").trim();
                        job.setJobID(jobID);
                        System.out.println("Submitted " + job + '+' + job.getDelay() + "sec as " + jobID);
                        if (config.notifyFreamon()) {
                            Freamon.onSubmit(job.getJobID());
                        }
                    }

                    if (line.contains("All TaskManagers are connected")) {
                        startTime = System.currentTimeMillis();
                        if (config.notifyFreamon()) {
                            Freamon.onStart(job.getJobID(), startTime);
                        }
                    }

                    if (line.contains("The following messages were created by the YARN cluster while running the Job:")) {
                        long endTime = System.currentTimeMillis();
                        long duration = (endTime - startTime);
                        System.out.println("Took " + duration / 1000
                            + " seconds to complete executing " + job + '+' + job.getDelay() + "sec");
                        if (config.notifyFreamon()) {
                            Freamon.onStop(job.getJobID(), endTime);
                        }
                    }
                }
                // TODO: create experiment summary file/output
                logPrintStream.flush();
                logPrintStream.close();

                while ((line = buffErr.readLine()) != null) System.out.println("[STDERR] " + line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
