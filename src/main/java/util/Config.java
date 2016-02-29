package util;

import parser.ConfigParser;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Johannes on 29/02/16.
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

    public Config(File configFile) {
        config = ConfigParser.parseConfig(configFile);
    }

    // TODO: once all the parameters are know this should be made more precise
    public String getConfigItem(String key) {
        return (String) config.get(key);
    }
}
