package Core.commandBuilder;

import akka.io.Dns;
import org.apache.log4j.Logger;
import util.Argument;
import java.util.ArrayList;

/**
 * Created by joh-mue on 23/02/16.
 */
public abstract class CommandBuilder {
    final static Logger LOG = Logger.getLogger(CommandBuilder.class);

    public static CommandBuilder getCommandBuilder(String runner) {
        switch (runner) {
            case "flink": return new FlinkCommandBuilder();
            case "spark": return new SparkCommandBuilder();
        default:
            LOG.error("There is no CommandBuilder defined for the runner " + runner + ". Check the runner spelling " +
                    "in your job definition.");
            System.exit(0);
        }
        return null;
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

    @Deprecated
    public abstract String getStartLine();
    @Deprecated
    public abstract String getStopLine();
    @Deprecated
    public abstract String getSubmittedLine();

    public abstract String getRunnerPrefix();
    public abstract boolean isStartLine(String logLine);
    public abstract boolean isStopLine(String logLine);
    public abstract boolean isSubmittedLine(String logLine);
}
