package Core.commandBuilder;

import util.Argument;
import java.util.ArrayList;

/**
 * Created by joh-mue on 23/02/16.
 */
public abstract class CommandBuilder {
    public static CommandBuilder getCommandBuilder(String runner) throws Exception {
        switch (runner) {
            case "flink": return new FlinkCommandBuilder();
            case "spark": return new SparkCommandBuilder();
        default:
            throw new Exception("There is no CommandBuilder defined for the runner " + runner + ". Check the runner spelling " +
                    "in your job definition.");
        }
    }

    /**
     * Concats the arguments and returns them as one string.
     * The String that will be returned starts and ends with a whitespace (" ")
     * @param arguments
     * @return String starting and ending with whitespace
     */
    protected String concatRunnerArguments(ArrayList<Argument> arguments) {
        String finalString = " ";
        for (Argument argument : arguments) {
            finalString += getRunnerPrefix() + argument + " ";
        }
        return finalString;
    }

    protected String concatJarArguments(ArrayList<Argument> arguments) {
        String finalString = " ";
        for (Argument argument : arguments) {
            finalString += argument + " ";
        }
        return finalString;
    }

    public abstract String getCommand(ArrayList<Argument> runnerArguments, String jarFile, ArrayList<Argument> jarArguments);

    public abstract String getStartLine();
    public abstract String getStopLine();
    public abstract String getSubmittedLine();
    public abstract String getRunnerPrefix();
}
