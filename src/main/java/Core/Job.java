package Core;

import Core.commandBuilder.CommandBuilder;

import java.util.*;

/**
 * Created by Johannes on 01/02/16.
 */
public class Job {

    protected String jarFile;
    protected String runnerArguments;
    protected String jarArguments;
    protected CommandBuilder cmdBuilder;
    protected String runner;
    protected String jobName;

    protected String JobID;

    public Job() {
        // TODO: optimize default constructor
        runnerArguments = "";
        jarArguments = "";
    }

    public String getCommand() {
        return cmdBuilder.getCommand(runnerArguments, jarFile, jarArguments);
    }

    private void setCmdBuilder(CommandBuilder cmdBuilder) {
        this.cmdBuilder = cmdBuilder;
    }

    public void addRunnerArgument(String argument, String value) {
        runnerArguments += argument + " " + value + " ";
    }

    public void addJarArgument(String argument, String value) {
        jarArguments += argument + " " + value + " ";
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
        for (String argument : runnerArguments) {
            addRunnerArgument(argument, "");
        }
    }

    public String getRunnerArguments() {
        return runnerArguments;
    }

    public void setJarArguments(ArrayList<String> jarArguments) {
        for (String argument: jarArguments) {
            addJarArgument(argument, "");
        }
    }

    public String getJarArguments() {
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
