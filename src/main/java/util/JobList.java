package util;

import Core.Job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Johannes on 10/02/16.
 */
public class JobList extends ArrayList<Job> {
    public Job getJobWithName(String jobName) {
        Iterator<Job>  jobIterator = this.iterator();
        while (jobIterator.hasNext()) {
            Job job = jobIterator.next();
            if (jobName.equalsIgnoreCase(job.getJobName())) {
                return job;
            }
        }
        throw new NoSuchElementException(jobName + " is not in the List of jobs.");
    }
}
