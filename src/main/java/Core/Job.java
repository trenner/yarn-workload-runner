package Core;

import java.io.File;

/**
 * Created by Johannes on 01/02/16.
 */
public abstract class Job { // TODO: make it abstract and then specific flink job, spark job, etc.

    private String jarFile;
    private String mainClass;
    private String arguments;
    private String runner;

    private String JobID;
    private long delay;

    public Job() {
        // TODO: fix default constructor
    }

    public Job(String jarFile, String mainClass, String arguments) {
        this.jarFile = jarFile;
        this.mainClass = mainClass;
        this.arguments = arguments;
    }

    public abstract String getCommand();

    public void addParameter(String argument, String value) {
        arguments = arguments + " " + argument + " " + value;
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

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
