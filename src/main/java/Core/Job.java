package Core;

/**
 * Created by Johannes on 01/02/16.
 */
public abstract class Job { // TODO: make it abstract and then specific flink job, spark job, etc.

    protected String jarFile;
    protected String mainClass;
    protected String arguments;
    protected String runner;
    protected String jobName;

    protected String JobID;

    public Job() {
        // TODO: fix default constructor
        arguments = "";
    }

    public abstract String getCommand();

    public void addParameter(String argument, String value) {
        arguments += argument + " " + value + " ";
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
