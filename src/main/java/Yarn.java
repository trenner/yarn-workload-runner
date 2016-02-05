/**
 * Created by Johannes on 01/02/16.
 */
public class Yarn {

    // yarn config
    // hadoop_home
    //

    public Yarn() {

    }

    public boolean submitApplication(YarnApp app) {
        try {
            Runtime.getRuntime().exec(app.getCommand());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
