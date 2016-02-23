package Core;

/**
 * Created by Johannes on 08/02/16.
 */
public class FlinkJob extends Job {

    @Override
    public String getCommand() {
        // TODO: systemHome should be read from config
        String systemHome = "/home/trenner/peel-experiments/ioaware-scheduling/systems/flink-0.10.1";
        return systemHome + "/bin/flink run " + runnerArguments + " " + jarFile + " " + jarArguments;
    }
}
