package Core.commandBuilder;

import util.Config;

import java.util.ArrayList;

/**
 * Created by Johannes on 08/02/16.
 */
public class FlinkCommandBuilder implements CommandBuilder {

    @Override
    public String getCommand(ArrayList<String> runnerArguments, String jarFile, ArrayList<String> jarArguments) {
        String flinkHome = Config.getInstance().getConfigItem("flink-home-dir");

        if (jarFile.charAt(0) == '.') { // if the jarfile is relative (start with '.') it will be relative to flinkhome
            jarFile = flinkHome + "/" + jarFile.substring(1);
        }

        return flinkHome + "/bin/flink run " + concatArguments(runnerArguments) + jarFile + concatArguments(jarArguments);
    }

    public String concatArguments(ArrayList<String> arguments) {
        String finalString = "";
        for (String argument : arguments) {
            finalString += argument + " ";
        }
        return finalString;
    }
}
