package Core;

import Core.commandBuilder.CommandBuilder;
import util.Tuple3;

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
    private ArrayList<Tuple3> runnerArguments;
    private ArrayList<Tuple3> jarArguments;
    private CommandBuilder cmdBuilder;
    private String runner;
    private String jobName;
    private String experimentName;
    private Integer duplicateNumber; // to make the logName unique
    private Long delay;
    private String JobID;

    /* Constructors */

    public Job(String jarFile, ArrayList<Tuple3> runnerArguments, ArrayList<Tuple3> jarArguments, CommandBuilder cmdBuilder,
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

    public String getCommand() {
        return cmdBuilder.getCommand(runnerArguments, jarFile, jarArguments);
    }

    private void setArgument(String value, ArrayList<Tuple3> arguments) {
        setArgument("", value, arguments);
    }

    private void setArgument(String key, String value, ArrayList<Tuple3> arguments) {
        if (key.isEmpty()) {
            arguments.add(new Tuple3(key, "", value));
        } else {
            for (Tuple3 tuple : arguments) {
                if (tuple.getKey().equalsIgnoreCase(key)) {
                    tuple.setValue(value);
                }
            }
        }
    }

    private void setOrOverwriteArguments(ArrayList<Tuple3> arguments, ArrayList<Tuple3> newArguments) {
        if (arguments.isEmpty()) {
            arguments.addAll(newArguments);
        } else {
            ArrayList<Tuple3> argumentsToAdd = new ArrayList<>();
            for (Tuple3 newTuple: newArguments) {
                if (newTuple.getKey().isEmpty()) {
                    arguments.add(newTuple);
                } else {
                    for (Tuple3 tuple: arguments) {
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

    public void setRunnerArguments(ArrayList<Tuple3> runnerArguments) {
        this.runnerArguments = runnerArguments;
    }

    public void setOrOverwriteRunnerArguments(ArrayList<Tuple3> newArguments) {
        setOrOverwriteArguments(runnerArguments, newArguments);
    }

    public ArrayList<Tuple3> getRunnerArguments() {
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

    public void setJarArguments(ArrayList<Tuple3> jarArguments) {
        this.jarArguments = jarArguments;
    }

    public void setOrOverwriteJarArguments(ArrayList<Tuple3> newArguments) {
        setOrOverwriteArguments(jarArguments, newArguments);
    }

    public ArrayList<Tuple3> getJarArguments() {
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
}
