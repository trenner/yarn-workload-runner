package Core;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import parser.ScheduleParser;
import util.Config;
import util.JobFactory;
import util.Schedule;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class YarnTimedGenerator {

    final static Logger LOG = Logger.getLogger(YarnTimedGenerator.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();

        // TODO: automatically include version number
        LOG.info("Yarn timed Generator v0.5.1-alpha. \n" +
                "Commit name 'updated README for arg-overwrite and job-sequences'. \n");
        // TODO: take default values and overwrite them if necessary

        // Load Jobs from jobfile
        File jobFile = new File(args[0]);
        JobFactory jobFactory = new JobFactory(jobFile);

        // TODO: load config from standard location
        // Load Config(s)
        File configFile = new File(args[2]);
        Config.initializeConfig(configFile);

        // Load Schedule
        File scheduleFile = new File(args[1]);
        ArrayList<Schedule> experimentSchedules = ScheduleParser.parseSchedule(scheduleFile, jobFactory);

        // check if they exist and create log directories
        createLogDirectories(experimentSchedules.iterator());

        // run jobs according to schedule
        Iterator<Schedule> experimentScheduleIterator = experimentSchedules.iterator();
        while (experimentScheduleIterator.hasNext()) {
            try {
                Schedule schedule = experimentScheduleIterator.next();
                String logFileName = Config.getLogDir(schedule.getExperimentName()) + "/times.log";
                PrintStream summaryLog = new PrintStream(new FileOutputStream(logFileName), true);

                summaryLog.println("Starting log.");

                //run with dstat
                if (Config.getInstance().runDstat()) {
                    //start  dstat
                    String dstatFilePath = Config.getLogDir(schedule.getExperimentName()) + "/dstats/";
                    Process[] dstatArr = startDstat(dstatFilePath);

                    Yarn yarn = new Yarn(schedule, summaryLog);
                    yarn.initiateJobExecution();

                    //stop dstat
                    stopDstat(dstatArr);
                } else {
                    Yarn yarn = new Yarn(schedule, summaryLog);
                    yarn.initiateJobExecution();
                }
                summaryLog.flush();
                summaryLog.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // TODO: wait until the last experiment finished before starting the next one
        }
    }

    private static void createLogDirectories(Iterator<Schedule> expScheduleIterator) {
        while (expScheduleIterator.hasNext()) {
            // TODO: only usage of Schedule class,if this is replaced, schedule class is no longer needed
            File expLogDir = Config.getLogDir(expScheduleIterator.next().getExperimentName());
            if (expLogDir.exists()) {
                if (Config.getInstance().overwriteLogs()) {
                    delete(expLogDir);
                } else {
                    LOG.error("Log directory " + expLogDir +
                            " already exists. Move or delete those logs first," +
                            " or change the log directory in your config.xml");
                    System.exit(1);
                }
            }
            expLogDir.mkdirs();
        }
    }

    /**
     * Starts dstat on all slave nodes and returns all processes collecting dstat data
     */
    private static Process[] startDstat(String logFolder) throws IOException {
        String dstatFilePath = logFolder;
        //create log dirs
        Runtime.getRuntime().exec("mkdir -p " + dstatFilePath, new String[0]);

        final String[] slaves = Config.getInstance().getSlaves();
        final String dstatCmd = Config.getInstance().getDstatCmd();
        Process[] dstatProcessArr = new Process[slaves.length];

        for (int i = 0; i < slaves.length; i++) {
            String slave = slaves[i];
            dstatProcessArr[i] = Runtime.getRuntime().exec("ssh " + slave + " " + dstatCmd + dstatFilePath + "/dstat-" + slave + ".csv");
        }
        return dstatProcessArr;
    }

    /**
     * Stops all dstat processes
     */
    private static void stopDstat(Process[] dstatProcessArr) {
        for (Process process : dstatProcessArr) {
            process.destroy();
        }
    }

    /**
     * Delete all files (not directories) in this directory and the directory itself if it is empty. If the directory
     * contains any folders the directory and the contained folders will remain. All filles in the dir will be deleted though.
     *
     * @param dir
     * @return
     */
    private static boolean delete(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        boolean deleted = dir.delete();
        LOG.info("Existing logs in " + dir + " deleted: " + deleted);
        return deleted;
    }
}
