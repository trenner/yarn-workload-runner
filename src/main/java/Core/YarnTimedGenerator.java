package Core;

import java.util.ArrayList;

public class YarnTimedGenerator implements Runnable {

    public void run() {
        System.out.println("Thread running.");

        // Load Jobs
        ArrayList<Job> jobs = new ArrayList<Job>();
        jobs.add(new FlinkJob());

        // Load Config(s)

        // Load Schedule
        ArrayList<Long> delays = new ArrayList<Long>();
        // add delays

        int i = 0;
        for (Job job : jobs) {
            job.setDelay(delays.get(i++));
        }

        YarnTimedGenerator runner = new YarnTimedGenerator();
        runner.run();

        // collect logs

        // run jobs according to schedule
        Yarn yarn = new Yarn();
        for (Job job: jobs) {
            try {
                wait(job.getDelay());
            } catch (Exception e) {e.printStackTrace();}
            yarn.submitApplication(job);
        }
    }

    public static void main(String[] args) {
        System.out.println("Running.");
        (new Thread(new YarnTimedGenerator())).start();
    }
}
