package Core;

import Core.commandBuilder.CommandBuilder;

import java.util.*;

/**
 * Created by Johannes on 01/02/16.
 */
public class Job {

    protected String jarFile;
    protected ArrayList<String> runnerArguments;
    protected ArrayList<String> jarArguments;
    protected CommandBuilder cmdBuilder;
    protected String runner;
    protected String jobName;

    protected String JobID;

    public Job() {
        // TODO: optimize default constructor
    }

    public String getCommand() {
        return cmdBuilder.getCommand(runnerArguments, jarFile, jarArguments);
    }

    private void setCmdBuilder(CommandBuilder cmdBuilder) {
        this.cmdBuilder = cmdBuilder;
    }

    /**
     * Sets the runner and picks the appropriate CommandBuilder.
     * @param runner
     */
    public void setRunner(String runner) {
        this.runner = runner;
        try {
            cmdBuilder = CommandBuilder.getCommandBuilder(runner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void setRunnerArguments(ArrayList<String> runnerArguments) {
        this.runnerArguments = runnerArguments;
    }

    public ArrayList<String> getRunnerArguments() {
        return runnerArguments;
    }

    public void setJarArguments(ArrayList<String> jarArguments) {
        this.jarArguments = jarArguments;
    }

    public ArrayList<String> getJarArguments() {
        return jarArguments;
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
