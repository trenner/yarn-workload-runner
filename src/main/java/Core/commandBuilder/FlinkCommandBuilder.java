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
        String flinkHome = Config.getInstance().getFlinkHome();

        if (jarFile.charAt(0) == '.') { // if the jarfile is relative (start with '.') it will be relative to flinkhome
            jarFile = flinkHome + "/" + jarFile.substring(1);
        }

        return flinkHome + "/bin/flink run " + concatRunnerArguments(runnerArguments) + jarFile + concatJarArguments(jarArguments);
    }

    @Override
    public String getRunnerPrefix() {
        return "-";
    }

    @Override
    public String extractJobID(String logLine) {
        return logLine.substring(logLine.indexOf("application_")).trim();
    }

    @Override
    public boolean isStartLine(String logLine) {
        boolean isSL = logLine.contains("Job execution switched to status RUNNING.")
                || logLine.contains("The following messages were created by the YARN cluster while running the Job:");
        return isSL;
    }

    @Override
    public boolean isStopLine(String logLine) {
        return logLine.contains("Job execution switched to status FINISHED.")
                || logLine.contains("All TaskManagers are connected");
    }

    @Override
    public boolean isSubmittedLine(String logLine) {
        return logLine.contains("Submitted application");
    }
}
