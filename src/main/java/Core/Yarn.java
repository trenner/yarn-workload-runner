package Core;

import Core.modules.Freamon;
import org.apache.log4j.Logger;
import util.Config;
import util.Schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Created by joh-mue on 01/02/16.
 */
public class Yarn {
    private Schedule schedule;
    private PrintStream summaryLog;
    //   private String dstatCmd = "dstat --epoch --cpu -C total --mem --net -N total --disk -D total --noheaders --nocolor --output ";
    //   private String[] slaves = {"wally071", "wally072", "wally073", "wally074", "wally075", "wally076", "wally077", "wally078", "wally079", "wally080", "wally081", "wally082", "wally083", "wally084", "wally085", "wally086", "wally087", "wally088", "wally059", "wally090", "wally091", "wally092", "wally093", "wally094", "wally095", "wally096", "wally097", "wally098", "wally099", "wally100", "wally101", "wally060", "wally103", "wally104", "wally105", "wally106", "wally107", "wally108", "wally109", "wally110"};
    private String[] slaves = Config.getInstance().getSlaves();
    final private String dstatCmd = Config.getInstance().getDstatCmd();

    final static Logger LOG = Logger.getLogger(Yarn.class);

    public Yarn(Schedule schedule, PrintStream summaryLog) {
        this.schedule = schedule;
        this.summaryLog = summaryLog;
    }

    public void initiateJobExecution() {
        if (Config.getInstance().runDstat()) {
            try {
                // start with dstat
                Process[] dstatProcessArr = new Process[slaves.length];
                Runtime.getRuntime().exec("mkdir -p" + Config.getInstance().getDstatCmd() + Config.getInstance().getConfigItem("log-dir") + "/" + schedule.getExperimentName() + "/dstats/", new String[0]);
                for (int i = 0; i < slaves.length; i++) {
                    String slave = slaves[i];
                    LOG.info("start dstat with: ssh " + slave + " " + dstatCmd + Config.getInstance().getConfigItem("log-dir") + "/" + schedule.getExperimentName() + "/dstats/dstat-" + slave + ".csv");
                    dstatProcessArr[i] = Runtime.getRuntime().exec("ssh " + slave + " " + dstatCmd + Config.getInstance().getConfigItem("log-dir") + "/" + schedule.getExperimentName() + "/dstats/dstat-" + slave + ".csv");
                }

                for (JobSequence jobSequence : schedule) {
                    Thread jobThread = new Thread(new JobRunner(jobSequence));
                    jobThread.start();
                }
                for (Process process1 : dstatProcessArr) {
                    process1.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //start without dstat
            for (JobSequence jobSequence : schedule) {
                Thread jobThread = new Thread(new JobRunner(jobSequence));
                jobThread.start();
            }
        }
    }

    private class JobRunner implements Runnable {
        private JobSequence jobSequence;
        private String[] envp;
        private Config config;

        public JobRunner(JobSequence jobSequence) {
            this.jobSequence = jobSequence;
            config = Config.getInstance();
            envp = new String[]{"HADOOP_CONF_DIR=" + config.getHadoopConfDir()};
        }

        public void run() {
            for (Job job : jobSequence) {
                executeJob(job);
            }


        }

        private void executeJob(Job job) {
            try {

                LOG.info("Waiting " + job.getDelay() + "seconds to execute " + job.getJobName());
                summaryLog.println("Waiting " + job.getDelay() + "seconds to execute " + job.getJobName());
                Thread.sleep(job.getDelay() * 1000, 0);

                PrintStream logPrintStream = job.getLogPrintStream(Config.getLogDir(job.getExperimentName()));

                LOG.info("Executing " + job + '+' + job.getDelay() + "sec with command: " + job.getCommand());

                Process process = Runtime.getRuntime().exec(job.getCommand(), envp);

                BufferedReader buff = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader buffErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                String line;
                long startTime = 0;
                boolean jobStarted = false;
                while ((line = buff.readLine()) != null) {
                    logPrintStream.println(line);
                    summaryLog.println("Analyzing job output.");
                    if (!jobStarted && job.getCmdBuilder().isSubmittedLine(line)) {
                        String jobID = line.substring(line.indexOf("Submitted application")).replace("Submitted application", "").trim();
                        job.setJobID(jobID);
                        LOG.info("Submitted " + job + '+' + job.getDelay() + "sec as " + jobID);
                        if (config.notifyFreamon()) {
                            Freamon.onSubmit(job.getJobID());
                        }
                    }

                    // once the job is marked as started don't check for the started line anymore
                    if (!jobStarted && job.getCmdBuilder().isStartLine(line)) {
                        startTime = System.currentTimeMillis();
                        if (config.notifyFreamon()) {
                            Freamon.onStart(job.getJobID(), startTime);
                        }
                        jobStarted = true;
                    }

                    if (jobStarted && job.getCmdBuilder().isStopLine(line)) {
                        long endTime = System.currentTimeMillis();
                        long duration = (endTime - startTime);
                        LOG.info("Took " + duration / 1000
                                + " seconds to complete executing " + job + '+' + job.getDelay() + "sec");
                        summaryLog.println("[" + Thread.currentThread().getName() + "]" + job.getJobName() + " - " + duration);
                        if (config.notifyFreamon()) {
                            Freamon.onStop(job.getJobID(), endTime);
                        }
                        jobStarted = false;
                    }
                }


                // TODO: create experiment summary file/output
                logPrintStream.flush();
                logPrintStream.close();

                summaryLog.flush();
                while ((line = buffErr.readLine()) != null) System.out.println("[STDERR] " + line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
