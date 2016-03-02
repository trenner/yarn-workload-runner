package Core;

import util.Config;
import util.JobList;
import util.JobTime;
import util.Schedule;

import java.io.*;

/**
 * Created by Johannes on 01/02/16.
 *
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

                PrintStream out = createLogPrintStream();

                System.out.println("Executing " + job + '+' + jobTime.getDelay() + "sec with command: " + job.getCommand());
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
                        System.out.println("Executing " + job + '+' + jobTime.getDelay() + "sec took " + duration / 1000000000 + " seconds to complete.");
                    }
                }
                // TODO: create experiment summary file/output
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private PrintStream createLogPrintStream() {

            String logDir = Config.getInstance().getConfigItem("log-dir") + '/' + schedule.getExperimentName();
            String logFileName = schedule.getExperimentName() + '-' + job.getJobName() + "+" + jobTime.getDelay();

            File experimentLogDir = new File(logDir);
            experimentLogDir.mkdirs(); // create directories if necessary

            try {
                return new PrintStream(new FileOutputStream(logDir + '/' + logFileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
