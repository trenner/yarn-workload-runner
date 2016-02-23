package Core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Johannes on 01/02/16.
 */
public abstract class Job { // TODO: make it abstract and then specific flink job, spark job, etc.

    protected String jarFile;
    protected String runnerArguments;
    protected String jarArguments;
    protected String runner;
    protected String jobName;

    protected String JobID;

    public Job() {
        // TODO: fix default constructor
        runnerArguments = "";
        jarArguments = "";
    }

    public abstract String getCommand();

    public void addRunnerArgument(String argument, String value) {
        runnerArguments += argument + " " + value + " ";
    }

    public void addJarArgument(String argument, String value) {
        jarArguments += argument + " " + value + " ";
    }

    public void setRunnerArguments(HashMap runnerArguments) {
        Iterator argIterator = runnerArguments.entrySet().iterator();
        while (argIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) argIterator.next();
            addRunnerArgument((String) entry.getKey(), (String) entry.getValue());
            argIterator.remove(); // avoids a ConcurrentModificationException
        }
    }

    public void setJarArguments(HashMap jarArguments) {
        Iterator argIterator = jarArguments.entrySet().iterator();
        while (argIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) argIterator.next();
            addJarArgument((String) entry.getKey(), (String) entry.getValue());
            argIterator.remove(); // avoids a ConcurrentModificationException
        }
    }

    public String getJarFile() {
        return jarFile;
    }

    public void setJarFile(String jarFile) {
        this.jarFile = jarFile;
    }

    public String getJobID() {
        return JobID;
    }

    public void setJobID(String jobID) {
        JobID = jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        return "Job{" +
                "runner='" + runner + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jarFile='" + jarFile + '\'' +
                '}';
    }
}
