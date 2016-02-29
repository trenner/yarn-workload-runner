package Core;

import parser.JobParser;
import parser.ScheduleParser;
import util.Config;
import util.JobList;
import util.Schedule;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class YarnTimedGenerator {
    public static void main(String[] args) {
        System.out.println("Yarn timed Generator.");

        // Load Jobs from jobfile
        File jobFile = new File(args[0]);
        JobList jobs = JobParser.parseJobs(jobFile);

        // Load Config(s)
        File configFile = new File(args[2]);
        Config.initializeConfig(configFile);

        // Load Schedule
        File scheduleFile = new File(args[1]);
        ArrayList<Schedule> experimentSchedules = ScheduleParser.parseSchedule(scheduleFile);

        // run jobs according to schedule
        Iterator<Schedule> jobScheduleIterator = experimentSchedules.iterator();
        while (jobScheduleIterator.hasNext()) {
            Yarn yarn = new Yarn(jobScheduleIterator.next(), jobs);
            yarn.runJobs();
            // TODO: wait until the last experiment finished
        }
    }
}
