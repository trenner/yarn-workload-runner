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
        String sparkHome = Config.getInstance().getConfigItem("spark-home-dir");

        String command = sparkHome + "/bin/spark-submit" + "--master yarn --deploy-mode cluster" + concatRunnerArguments(runnerArguments) + jarFile + concatJarArguments(jarArguments);
        return null;
    }

    @Override
    public String getStartLine() {
        return null;
    }

    @Override
    public String getStopLine() {
        return null;
    }

    @Override
    public String getSubmittedLine() {
        return null;
    }

    @Override
    public String getRunnerPrefix() {
        return "--";
    }
}
