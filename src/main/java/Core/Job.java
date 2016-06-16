package Core;

import Core.commandBuilder.CommandBuilder;
import util.Argument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Created by joh-mue on 01/02/16.
 */
public class Job {

    private String jarFile;
    private ArrayList<Argument> runnerArguments;
    private ArrayList<Argument> jarArguments;
    private CommandBuilder cmdBuilder;
    private String runner;
    private String jobName;
    private String experimentName;
    private Integer duplicateNumber; // to make the logName unique
    private Long delay;
    private String jobID;

    /* Constructors */

    public Job(String jarFile, ArrayList<Argument> runnerArguments, ArrayList<Argument> jarArguments, CommandBuilder cmdBuilder,
               String runner, String jobName, Integer duplicateNumber, Long delay, String experimentName) {
        this.jarFile = jarFile;
        this.runnerArguments = runnerArguments;
        this.jarArguments = jarArguments;
        this.cmdBuilder = cmdBuilder;
        this.runner = runner;
        this.jobName = jobName;
        this.duplicateNumber = duplicateNumber;
        this.delay = delay;
        this.experimentName = experimentName;
    }

    /* functionality */

    /**
     * Returns appropriate, unique PrintStream in the log directory for this job.
     *
     * @param logDir the directory where the log should be created, usually per Config.getLogDir(Schedule experiment)
     * @return PrintStream for this job or null in case of FileNotFoundException
     */
    public PrintStream getLogPrintStream(File logDir) {
        final String logName = String.format("%s-%s+%s(%s).out", experimentName, jobName, delay, duplicateNumber);
        File logFile = new File(logDir, logName);
        try {
            return new PrintStream(new FileOutputStream(logFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Job{" +
                "runner='" + runner + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jarFile='" + jarFile + '\'' +
                '}';
    }

    /**
     * Returns a copy form this prototype with different values for experimentName, jobName and delay
     *
     * @param experimentName
     * @param jobName
     * @param delay
     * @return
     */
    public Job cloneAndSet(String experimentName, String jobName, long delay, Integer duplicateNumber) {
        return new Job(jarFile, runnerArguments, jarArguments, cmdBuilder,
                runner, jobName, duplicateNumber, delay, experimentName);
    }

    /* getter and setter */

    /**
     * Sets the runner and picks the appropriate CommandBuilder.
     *
     * @param runner
     */
    public void setRunner(String runner) {
        this.runner = runner;
        cmdBuilder = CommandBuilder.getCommandBuilder(runner);
    }

    public CommandBuilder getCmdBuilder() {
        return cmdBuilder;
    }

    public String getCommand() {
        return cmdBuilder.getCommand(runnerArguments, jarFile, jarArguments);
    }

    private void setArgument(String value, ArrayList<Argument> arguments) {
        setArgument("", value, arguments);
    }

    private void setArgument(String key, String value, ArrayList<Argument> arguments) {
        if (key.isEmpty()) {
            arguments.add(new Argument(key, value));
        } else {
            for (Argument tuple : arguments) {
                if (tuple.getKey().equalsIgnoreCase(key)) {
                    tuple.setValue(value);
                }
            }
        }
    }

    private void setOrOverwriteArguments(ArrayList<Argument> arguments, ArrayList<Argument> newArguments) {
        if (arguments.isEmpty()) {
            arguments.addAll(newArguments);
        } else {
            // TODO: [!] why is this variable never used??
            ArrayList<Argument> argumentsToAdd = new ArrayList<>();
            for (Argument newArgument : newArguments) {
                if (newArgument.getKey().isEmpty()) {
                    arguments.add(newArgument);
                } else {
                    for (Argument argument : arguments) {
                        if (argument.getKey().equalsIgnoreCase(newArgument.getKey())) {
                            argument.setValue(newArgument.getValue());
                        }
                    }
                }
            }
        }
    }

    //TODO: This method is copied from the setOrOverwriteArguments method. We cannot parse jar argument names to the cmd builder
    private void setOrOverwriteJarArguments(ArrayList<Argument> arguments, ArrayList<Argument> newArguments) {
        if (arguments.isEmpty()) {
            arguments.addAll(newArguments);
        } else {
            // TODO: [!] why is this variable never used??
            ArrayList<Argument> argumentsToAdd = new ArrayList<>();
            for (Argument newArgument : newArguments) {
                if (newArgument.getKey().isEmpty()) {
                    arguments.add(newArgument);
                } else {
                    ArrayList<Argument> argsToAdd = new ArrayList<Argument>();
                    ArrayList<Argument> argsToRemove = new ArrayList<Argument>();
                    for (Argument argument : arguments) {
                        if (argument.getKey().equalsIgnoreCase(newArgument.getKey())) {
                            argsToAdd.add(new Argument("", newArgument.getValue()));
                            argsToRemove.add(argument);
                        }
                    }
                    arguments.removeAll(argsToRemove);
                    arguments.addAll(argsToAdd);
                }
            }
        }
    }

    /**
     * Sets or overwrites the argument with given key. Arguments without key cannot be overwritten, instead they will
     * be added to the list of arguments.
     *
     * @param key   - argument to be overwritten.
     * @param value
     */
    public void setRunnerArgument(String key, String value) {
        setArgument(key, value, runnerArguments);
    }

    public void setRunnerArguments(ArrayList<Argument> runnerArguments) {
        this.runnerArguments = runnerArguments;
    }

    public void setOrOverwriteRunnerArguments(ArrayList<Argument> newArguments) {
        setOrOverwriteArguments(runnerArguments, newArguments);
    }

    public ArrayList<Argument> getRunnerArguments() {
        return runnerArguments;
    }

    /**
     * Sets or overwrites all arguments with given key. Arguments without key cannot be overwritten, instead they will
     * simply be added to the list of arguments.
     *
     * @param key   - argument to be overwritten.
     * @param value
     */
    public void setJarArgument(String key, String value) {
        setArgument(key, value, jarArguments);
    }

    public void setJarArguments(ArrayList<Argument> jarArguments) {
        this.jarArguments = jarArguments;
    }

    public void setOrOverwriteJarArguments(ArrayList<Argument> newArguments) {
        setOrOverwriteJarArguments(jarArguments, newArguments);
    }

    public ArrayList<Argument> getJarArguments() {
        return jarArguments;
    }

    public String getJarFile() {
        return jarFile;
    }

    public void setJarFile(String jarFile) {
        this.jarFile = jarFile;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public Long getDelay() {
        return delay;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getRunnerArgumentPrefix() {
        return cmdBuilder.getRunnerPrefix();
    }
}
