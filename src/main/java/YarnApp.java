import java.io.File;

/**
 * Created by Johannes on 01/02/16.
 */
public class YarnApp {

    private String jarFile;
    private String mainClass;
    private String arguments;

    private String JobID;
    private long delay;


    public YarnApp(String jarFile, String mainClass, String arguments) {
        this.jarFile = jarFile;
        this.mainClass = mainClass;
        this.arguments = arguments;
    }

    // /bin/flink run -m yarn-cluster -yn 4 -yjm 1024 -ytm 4096 ./examples/WordCount.jar
    // yarn jar <jar> [mainClass] args...
    public String getCommand() {

        // TODO: put together the command

        String systemHome = "/Users/Johannes/arbeit/systems/flink-0.10.1";
        return systemHome + "/bin/flink run -m yarn-cluster -yn 4 -yjm 1024 -ytm 4096 " + systemHome + "/examples/WordCount.jar";
    }

    public String getJobID() {
        return JobID;
    }

    public void setJobID(String jobID) {
        JobID = jobID;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
