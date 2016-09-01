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
    public String getRunnerPrefix() {
        return "--";
    }

    @Override
    public String extractJobID(String logLine) {
        return logLine.substring(logLine.indexOf("application_")).trim();
    }

    @Override
    public boolean isStartLine(String logLine) {
        return logLine.contains("(state: RUNNING)");
    }

    @Override
    public boolean isStopLine(String logLine) {
        return logLine.contains("(state: FINISHED)");
    }

    @Override
    public boolean isSubmittedLine(String logLine) {
        return logLine.contains("Submitted application");
    }
}
