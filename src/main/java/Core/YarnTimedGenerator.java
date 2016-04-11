package Core;

import parser.ScheduleParser;
import util.Config;
import util.JobFactory;
import util.Schedule;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class YarnTimedGenerator {
    public static void main(String[] args) {
        System.out.println("Yarn timed Generator.");
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
            Yarn yarn = new Yarn(experimentScheduleIterator.next());
            yarn.runJobs();
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
                    System.out.println("Log directory " + expLogDir +
                            " already exists. Move or delete those logs first," +
                            " or change the log directory in your config.xml");
                    System.exit(1);
                }
            }
            expLogDir.mkdirs();
        }
    }

    /**
     * Delete all files (not directories) in this directory and the directory itself if it is empty. If the directory
     * contains any folders the directory and the contained folders will remain. All filles in the dir will be deleted though.
     * @param dir
     * @return
     */
    private static boolean delete(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file: files) {
                file.delete();
            }
        }
        boolean deleted = dir.delete();
        System.out.println("Existing logs in " + dir + " deleted: " + deleted);
        return deleted;
    }
}
