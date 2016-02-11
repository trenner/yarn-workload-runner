package Core;

/**
 * Created by Johannes on 08/02/16.
 */
public class FlinkJob extends Job {

    @Override
    public String getCommand() {
        String systemHome = "/Users/Johannes/arbeit/systems/flink-0.10.1";
        return systemHome + "/bin/flink run " + arguments + " " + systemHome + jarFile;
    }
}
