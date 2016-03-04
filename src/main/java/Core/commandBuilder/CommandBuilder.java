package Core.commandBuilder;

import java.util.ArrayList;

/**
 * Created by Johannes on 23/02/16.
 */
public interface CommandBuilder {
    String getCommand(ArrayList<String> runnerArguments, String jarFile, ArrayList<String> jarArguments);

    static CommandBuilder getCommandBuilder(String runner) throws Exception {
        // TODO:this is static, actually pick the CommandBuilder
        if (false) {
            throw new Exception("There is no CommandBuilder for the runner " + runner);
        }
        return new FlinkCommandBuilder();
    }
}
