package Core;

import parser.JobParser;

import java.io.File;
import java.util.ArrayList;

public class YarnTimedGenerator implements Runnable {

    public void run() {
        System.out.println("Thread running.");

        // Load Jobs from jobfile TODO:un-hardcode this
        File jobFile = new File("/Users/Johannes/arbeit/yarn-timed-workload-generator/src/main/resources/jobs.xml");
        ArrayList<Job> jobs = JobParser.parseJobs(jobFile);

        // Load Config(s)

        // Load Schedule
        ArrayList<Long> delays = new ArrayList<>();

        int i = 0;
        for (Job job : jobs) {
            job.setDelay(delays.get(i++));
        }

        // run jobs according to schedule
        // TODO: this needs to happen in different threads
        Yarn yarn = new Yarn();
        for (Job job: jobs) {
            try {
                wait(job.getDelay());
            } catch (Exception e) {e.printStackTrace();}
            yarn.submitApplication(job);
        }

        // collect logs
    }

    public static void main(String[] args) {
        System.out.println("Running.");
        // TODO: appropriate threading
        (new Thread(new YarnTimedGenerator())).start();
    }
}
