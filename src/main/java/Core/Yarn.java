package Core;

import util.JobList;
import util.JobTime;
import util.Schedule;

/**
 * Created by Johannes on 01/02/16.
 */
public class Yarn implements Runnable {
    private Schedule schedule;
    private JobList jobs;

    // TODO: abstract, flink runner, yarn runner, etc.
    public Yarn(Schedule schedule, JobList jobs) {
        this.schedule = schedule;
        this.jobs = jobs;
    }

    public void run() {
        for (JobTime jobTime: schedule) {
            try {
                System.out.println("Waiting " + jobTime.getDelay());
                Thread.sleep(jobTime.getDelay());
                Job job = jobs.getJobWithName(jobTime.getJobName());
                submitApplication(job);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private boolean submitApplication(Job job) {
        try {
            System.out.println("Executing " + job);
//            Runtime.getRuntime().exec(job.getCommand());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
