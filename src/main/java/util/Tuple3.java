package util;

/**
 * Created by joh-mue on 22/04/16.
 */
public class Tuple3 {
    private String key;
    private String prefix;
    private String value;

    public Tuple3(String key, String prefix, String value) {
        this.key = key;
        this.prefix = prefix;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return prefix + key + " " + value;

    }
}
