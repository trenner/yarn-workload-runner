package util;

import parser.ConfigParser;

import java.io.File;
import java.util.HashMap;

/**
 * Created by joh-mue on 29/02/16.
 */
public class Config {
    private static Config instance;
    private HashMap config;

    // TODO: not a proper singleton initialization
    public static void initializeConfig(File configFile) {
        if(instance == null){
            instance = new Config(configFile);
        }
    }

    public static Config getInstance() {
        // i threw up
        try {
            if (instance == null) {
                throw new Exception("The Config was not initialized yet.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    private Config(File configFile) {
        config = ConfigParser.parseConfig(configFile);
    }

    // TODO: [009a] once all the parameters are know this should be made more precise
    public String getConfigItem(String key) {
        return (String) config.get(key);
    }

    public static File getLogDir(String experimentName) {
        // TODO: [009b]should not call getInstance but be static instead
        String baseLogDir = Config.getInstance().getConfigItem("log-dir");
        return new File(baseLogDir + '/' + experimentName);
    }

    /**
     * Returns true if sequential execution is wanted false otherwise. Start delays will be relative
     * to the last job that was submitted.
     * @return boolean indicating if sequential execution is desired
     */
    public boolean sequentialExecution() {
        return getConfigItem("sequentialExecution").equalsIgnoreCase("true");
    }
}
