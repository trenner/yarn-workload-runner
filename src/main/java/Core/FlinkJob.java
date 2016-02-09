package Core;

/**
 * Created by Johannes on 08/02/16.
 */
public class FlinkJob extends Job {

    @Override
    public String getCommand() {
        // TODO: put together the command
        // /bin/flink run -m yarn-cluster -yn 4 -yjm 1024 -ytm 4096 ./examples/WordCount.jar
        // yarn jar <jar> [mainClass] args...
        String systemHome = "/Users/Johannes/arbeit/systems/flink-0.10.1";
        return systemHome + "/bin/flink run" + arguments + " " + systemHome + jarFile;
    }
}
