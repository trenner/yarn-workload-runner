package Core;

import Core.commandBuilder.CommandBuilder;
import util.Argument;

import java.util.ArrayList;

/**
 * Created by joh-mue on 15/06/16.
 */
public class JobDefinition {

    private String jarFile;
    private ArrayList<Argument> runnerArguments;
    private ArrayList<Argument> jarArguments;
    private String runner;
    private CommandBuilder cmdBuilder;
    private String jobName;

    public JobDefinition(String jarFile, ArrayList<Argument> runnerArguments, ArrayList<Argument> jarArguments, String runner, String jobName) {
        this.jarFile = jarFile;
        this.runnerArguments = runnerArguments;
        this.jarArguments = jarArguments;
        this.runner = runner;
        this.cmdBuilder = CommandBuilder.getCommandBuilder(runner);
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public Job createJob(String experimentName, Long delay, Integer count) {
        Job job = new Job(jarFile, copyArgumentList(runnerArguments), copyArgumentList(jarArguments), cmdBuilder,
                new String(runner), new String(jobName), count, delay, experimentName);
        return job;
    }

    /**
     * Creates a deep copy of a list of arguments.
     * @param originalList
     * @return
     */
    private ArrayList<Argument> copyArgumentList(ArrayList<Argument> originalList) {
        ArrayList<Argument> copiedList = new ArrayList<>();
        originalList.forEach(argument -> {
            copiedList.add(new Argument(new String(argument.getKey()), new String(argument.getValue())));
        });
        return copiedList;
    }
}
