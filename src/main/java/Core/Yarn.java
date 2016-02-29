package Core;

import util.JobList;
import util.JobTime;
import util.Schedule;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Johannes on 01/02/16.
 */
public class Yarn {
    private Schedule schedule;
    private JobList jobs;

    public Yarn(Schedule schedule, JobList jobs) {
        this.schedule = schedule;
        this.jobs = jobs;
    }

    public void runJobs() {
        for (JobTime jobTime: schedule) {
                Job job = jobs.getJobWithName(jobTime.getJobName());
                (new Thread(new JobRunner(job, jobTime))).start();
        }
    }

    private class JobRunner implements Runnable {

        private Job job;
        private JobTime jobTime;

        public JobRunner(Job job, JobTime jobTime) {
            this.job = job;
            this.jobTime = jobTime;
        }

        public void run() {
            try {
                System.out.println("Waiting " + jobTime.getDelay() + " to execute " + jobTime.getJobName());
                Thread.sleep(jobTime.getDelay() * 1000,0);
                System.out.println("Executing " + job + " with command: " + job.getCommand());

                InputStream inputStream = Runtime.getRuntime().exec(job.getCommand()).getInputStream();
                InputStreamReader isReader = new InputStreamReader(inputStream);
                BufferedReader buff = new BufferedReader(isReader);
                String line;
                while((line = buff.readLine()) != null) {
                    System.out.println(line);
                    // TODO: write log to disk (specifiy which job with name and)
                    if (line.contains("Submitted application")) {
                        String jobID = line.substring(line.indexOf("Submitted application")).replace("Submitted application","").trim();
                        job.setJobID(jobID);
                    }
                    // TODO: notify when job reaches status finished and take the time
                    // TODO: create experiment summary file/output
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
