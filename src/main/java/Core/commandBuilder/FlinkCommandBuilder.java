package Core.commandBuilder;

import util.Config;

/**
 * Created by Johannes on 08/02/16.
 */
public class FlinkCommandBuilder implements CommandBuilder {

    @Override
    public String getCommand(String runnerArguments, String jarFile, String jarArguments) {
        String flinkHome = Config.getInstance().getConfigItem("flink-home-dir");

        if (jarFile.charAt(0) == '.') { // if the jarfile is relative (start with '.') it will be relative to flinkhome
            jarFile = flinkHome + "/" + jarFile.substring(1);
        }

        return flinkHome + "/bin/flink run " + runnerArguments.trim() + " " + jarFile + " " + jarArguments.trim();
    }
}
