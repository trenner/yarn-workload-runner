package Core;

/**
 * Created by Johannes on 01/02/16.
 */
public class Yarn {

    // TODO: abstract, flink runner, yarn runner, etc.

    // yarn config
    // hadoop_home
    //

    public Yarn() {

    }

    public boolean submitApplication(Job app) {
        try {
            Runtime.getRuntime().exec(app.getCommand());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
