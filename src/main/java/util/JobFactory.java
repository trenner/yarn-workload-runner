package util;

import Core.Job;
import Core.JobDefinition;
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
    ArrayList<JobDefinition> jobDefinitions;
    HashMap<String, Integer> duplicateNumbers;

    public JobFactory(File jobFile) {
        jobDefinitions = JobParser.parseJobs(jobFile);
        duplicateNumbers = new HashMap<>();
    }

    public Job getJob(String experimentName, String jobName, Long delay) {
        Iterator<JobDefinition>  jobDefinitionIterator = jobDefinitions.iterator();
        while (jobDefinitionIterator.hasNext()) {
            JobDefinition jobDefinition = jobDefinitionIterator.next();

            if (jobName.equalsIgnoreCase(jobDefinition.getJobName())) {
                String jobID = experimentName + jobName + delay.toString();
                Integer count = duplicateNumbers.get(jobID);
                if (duplicateNumbers.containsKey(jobID)) {
                    duplicateNumbers.put(jobID, ++count);
                } else {
                    duplicateNumbers.put(jobID, 0);
                    count = 0;
                }
                return jobDefinition.createJob(experimentName, delay, count);
            }
        }
        throw new NoSuchElementException(jobName + " is not in the List of jobs.");
    }
}
