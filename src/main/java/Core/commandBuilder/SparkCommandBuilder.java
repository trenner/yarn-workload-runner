package Core.commandBuilder;

import util.Config;
import util.Argument;

import java.util.ArrayList;

/**
 * Created by joh-mue on 10/05/16.
 */
public class SparkCommandBuilder extends CommandBuilder {
    @Override
    public String getCommand(ArrayList<Argument> runnerArguments, String jarFile, ArrayList<Argument> jarArguments) {
        String sparkHome = Config.getInstance().getSparkHome();

        if (jarFile.charAt(0) == '.') { // if the jarfile is relative (start with '.') it will be relative to flinkhome
            jarFile = sparkHome + "/" + jarFile.substring(1);
        }

        return sparkHome + "/bin/spark-submit "
                + "--master yarn --deploy-mode cluster" + concatRunnerArguments(runnerArguments)
                + jarFile + concatJarArguments(jarArguments);
    }

    @Override
    public String getStartLine() {
        return "not yet set";
    }

    @Override
    public String getStopLine() {
        return "not yet set";
    }

    @Override
    public String getSubmittedLine() {
        return "not yet set";
    }

    @Override
    public String getRunnerPrefix() {
        return "--";
    }

    @Override
    public boolean isStartLine(String logLine) {
        return false;
    }

    @Override
    public boolean isStopLine(String logLine) {
        return false;
    }

    @Override
    public boolean isSubmittedLine(String logLine) {
        return false;
    }
}
