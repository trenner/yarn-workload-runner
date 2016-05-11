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
    private String JobID;

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

    /**
     * should not be used. jobs must be fully initialized
     */
    @Deprecated
    public Job() {
        // TODO: optimize default constructor
        duplicateNumber = 0;
        this.runnerArguments = new ArrayList<>();
        this.jarArguments = new ArrayList<>();
    }

    /* functionality */

    /**
     * Returns appropriate, unique PrintStream in the log directory for this job.
     *
     * @param logDir the directory where the log should be created, usually per Config.getLogDir(Schedule experiment)
     *
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
     * Returns a clone of this job.
     *
     * NOT READY for use
     * @return
     */
    @Override
    @Deprecated
    public Job clone() {
        ArrayList<Argument> runnerArgumentsClone = new ArrayList<>();
        runnerArguments.forEach( tuple3 -> runnerArgumentsClone.add(tuple3));

        Job jobClone = new Job(
                new String(jarFile),
                runnerArguments,
                jarArguments,
                cmdBuilder,
                new String(runner),
                new String(jobName),
                new Integer(duplicateNumber),
                new Long(delay),
                new String(experimentName));

        return jobClone;
    }

    /* getter and setter */

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
            ArrayList<Argument> argumentsToAdd = new ArrayList<>();
            for (Argument newTuple: newArguments) {
                if (newTuple.getKey().isEmpty()) {
                    arguments.add(newTuple);
                } else {
                    for (Argument tuple: arguments) {
                        if (tuple.getKey().equalsIgnoreCase(newTuple.getKey())) {
                            tuple.setValue(newTuple.getValue());
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets or overwrites the argument with given key. Arguments without key cannot be overwritten, instead they will
     * be added to the list of arguments.
     * @param key - argument to be overwritten.
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
     * @param key - argument to be overwritten.
     * @param value
     */
    public void setJarArgument(String key, String value) {
        setArgument(key, value, jarArguments);
    }

    public void setJarArguments(ArrayList<Argument> jarArguments) {
        this.jarArguments = jarArguments;
    }

    public void setOrOverwriteJarArguments(ArrayList<Argument> newArguments) {
        setOrOverwriteArguments(jarArguments, newArguments);
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
        return JobID;
    }

    public void setJobID(String jobID) {
        JobID = jobID;
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

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public void setDuplicateNumber(Integer duplicateNumber) {
        this.duplicateNumber = duplicateNumber;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }
}
