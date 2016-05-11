package Core.commandBuilder;
`
import util.Argument;
import java.util.ArrayList;

/**
 * Created by joh-mue on 23/02/16.
 */
public abstract class CommandBuilder {
    String startLine;
    String stopLine;
    String submittedLine;
    String runnerPrefix;

    public static CommandBuilder getCommandBuilder(String runner) throws Exception {
        // TODO: this is static, actually pick the CommandBuilder
        switch (runner) {
            case "flink": return new FlinkCommandBuilder();
            case "spark": return new SparkCommandBuilder();
        default:
            throw new Exception("There is no CommandBuilder for the runner " + runner);
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
            finalString += runnerPrefix + argument + " ";
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

    public abstract String getStartLine();
    public abstract String getStopLine();
    public abstract String getSubmittedLine();
    public abstract String getRunnerPrefix();
    public abstract String getCommand(ArrayList<Argument> runnerArguments, String jarFile, ArrayList<Argument> jarArguments);
}
