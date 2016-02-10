package Core;

import parser.JobParser;
import parser.ScheduleParser;
import util.JobList;
import util.Schedule;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class YarnTimedGenerator {

    // TODO:un-hardcode this
    // put the locations of your configs here
    private static final String JOB_FILE = "/Users/Johannes/arbeit/yarn-timed-workload-generator/src/main/resources/jobs.xml";
    private static final String SCHEDULE_FILE = "/Users/Johannes/arbeit/yarn-timed-workload-generator/src/main/resources/schedule.xml";


    public static void main(String[] args) {
        System.out.println("Yarn timed Generator.");

        System.out.println("Thread running.");

        // Load Jobs from jobfile
        File jobFile = new File(JOB_FILE);
        JobList jobs = JobParser.parseJobs(jobFile);

        // Load Config(s)

        // Load Schedule
        File scheduleFile = new File(SCHEDULE_FILE);
        ArrayList<Schedule> experimentSchedules = ScheduleParser.parseSchedule(scheduleFile);


        // run jobs according to schedule
        Iterator<Schedule> jobScheduleIterator = experimentSchedules.iterator();
        while (jobScheduleIterator.hasNext()) {
            Yarn yarn = new Yarn(jobScheduleIterator.next(), jobs);
            yarn.runJobs();
        }
    }
}
