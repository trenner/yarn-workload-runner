package Core.commandBuilder;

import util.Config;
import util.Argument;

import java.util.ArrayList;

/**
 * Created by Johannes on 08/02/16.
 */
public class FlinkCommandBuilder extends CommandBuilder {

    @Override
    public String getCommand(ArrayList<Argument> runnerArguments, String jarFile, ArrayList<Argument> jarArguments) {
        String flinkHome = Config.getInstance().getConfigItem("flink-home-dir");

        if (jarFile.charAt(0) == '.') { // if the jarfile is relative (start with '.') it will be relative to flinkhome
            jarFile = flinkHome + "/" + jarFile.substring(1);
        }

        return flinkHome + "/bin/flink run " + concatRunnerArguments(runnerArguments) + jarFile + concatJarArguments(jarArguments);
    }

    @Override
    public String getStartLine() {
        return "All TaskManagers are connected";
    }

    @Override
    public String getStopLine() {
        return "The following messages were created by the YARN cluster while running the Job:";
    }

    @Override
    public String getSubmittedLine() {
        return "Submitted application";
    }

    @Override
    public String getRunnerPrefix() {
        return "-";
    }
}
