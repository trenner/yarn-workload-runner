package Core;

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

    public void runJobs() {
        for (Job job: schedule) {
                (new Thread(new JobRunner(job))).start();
        }
    }

    private class JobRunner implements Runnable {

        private Job job;

        public JobRunner(Job job) {
            this.job = job;
        }

        public void run() {
            try {
                System.out.println("Waiting " + job.getDelay() + " to execute " + job.getJobName());
                Thread.sleep(job.getDelay() * 1000,0);

                PrintStream out = job.getLogPrintStream(Config.getLogDir(job.getExperimentName()));

                System.out.println("Executing " + job + '+' + job.getDelay() + "sec with command: " + job.getCommand());
                InputStream inputStream = Runtime.getRuntime().exec(job.getCommand()).getInputStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream));

                String line; long startTime = 0;
                while((line = buff.readLine()) != null) {
                    out.println(line);
                    if (line.contains("Submitted application")) {
                        String jobID = line.substring(line.indexOf("Submitted application")).replace("Submitted application","").trim();
                        job.setJobID(jobID);
                    }

                    if (line.contains("Job execution switched to status RUNNING.")) {
                        startTime = System.nanoTime();
                    }

                    if (line.contains("Job execution switched to status FINISHED")) {
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime);
                        System.out.println("Executing " + job + '+' + job.getDelay() + "sec took " + duration / 1000000000 + " seconds to complete.");
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
