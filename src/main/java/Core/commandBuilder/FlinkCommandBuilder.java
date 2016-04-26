package Core.commandBuilder;

import util.Config;
import util.Tuple3;

import java.util.ArrayList;

/**
 * Created by Johannes on 08/02/16.
 */
public class FlinkCommandBuilder implements CommandBuilder {

    @Override
    public String getCommand(ArrayList<Tuple3> runnerArguments, String jarFile, ArrayList<Tuple3> jarArguments) {
        String flinkHome = Config.getInstance().getConfigItem("flink-home-dir");

        if (jarFile.charAt(0) == '.') { // if the jarfile is relative (start with '.') it will be relative to flinkhome
            jarFile = flinkHome + "/" + jarFile.substring(1);
        }

        return flinkHome + "/bin/flink run " + concatArguments(runnerArguments) + jarFile + concatArguments(jarArguments);
    }

    public String concatArguments(ArrayList<Tuple3> arguments) {
        String finalString = " ";
        for (Tuple3 argument : arguments) {
            finalString += argument + " ";
        }
        return finalString;
    }
}
