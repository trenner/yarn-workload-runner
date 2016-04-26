package Core.commandBuilder;

import util.Tuple3;

import java.util.ArrayList;

/**
 * Created by joh-mue on 23/02/16.
 */
public interface CommandBuilder {
    String getCommand(ArrayList<Tuple3> runnerArguments, String jarFile, ArrayList<Tuple3> jarArguments);

    static CommandBuilder getCommandBuilder(String runner) throws Exception {
        // TODO: this is static, actually pick the CommandBuilder
        if (false) {
            throw new Exception("There is no CommandBuilder for the runner " + runner);
        }
        return new FlinkCommandBuilder();
    }
}
