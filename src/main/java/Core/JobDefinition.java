package Core;

import Core.commandBuilder.CommandBuilder;
import util.Tuple3;

import java.util.ArrayList;

/**
 * Created by joh-mue on 10/05/16.
 */
public class JobDefinition {

    private String jarFile;
    private ArrayList<Tuple3> runnerArguments;
    private ArrayList<Tuple3> jarArguments;
    private String runner;
    private CommandBuilder cmdBuilder;
    private String jobName;
    private String experimentName;
    private Integer duplicateNumber; // to make the logName unique
    private String JobID;

    public JobDefinition(String jarFile, ArrayList<Tuple3> runnerArguments, ArrayList<Tuple3> jarArguments, String runner, CommandBuilder cmdBuilder, String jobName, String experimentName, String jobID) {
        this.jarFile = jarFile;
        this.runnerArguments = runnerArguments;
        this.jarArguments = jarArguments;
        this.runner = runner;
        this.cmdBuilder = cmdBuilder;
        this.jobName = jobName;
        this.experimentName = experimentName;
        JobID = jobID;
    }
}
