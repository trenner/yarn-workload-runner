package util;

import Core.Job;
import parser.JobParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by joh-mue on 10/02/16.
 */
public class JobFactory extends ArrayList<Job> {
    ArrayList<Job> jobPrototypes;
    HashMap<String, Integer> duplicateNumbers;

    public JobFactory(File jobFile) {
        jobPrototypes = JobParser.parseJobs(jobFile);
        duplicateNumbers = new HashMap<>();
    }

    public Job getJob(String experimentName, String jobName, Long delay) {
        Iterator<Job>  jobPrototypeIterator = jobPrototypes.iterator();
        while (jobPrototypeIterator.hasNext()) {
            Job jobPrototype = jobPrototypeIterator.next();

            if (jobName.equalsIgnoreCase(jobPrototype.getJobName())) {
                String key = experimentName + jobName + delay.toString();
                Integer count = duplicateNumbers.get(key);
                if (duplicateNumbers.containsKey(key)) {
//                    count++;
                    duplicateNumbers.put(key, ++count);
                } else {
                    duplicateNumbers.put(key, 0);
                    count = 0;
                }
                return jobPrototype.cloneAndSet(experimentName, jobName, delay, count);
            }
        }
        throw new NoSuchElementException(jobName + " is not in the List of jobs.");
    }
}
