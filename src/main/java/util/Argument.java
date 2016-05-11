package util;

/**
 * Created by joh-mue on 22/04/16.
 */
public class Argument {
    private String key;
    private String value;

    public Argument(String key, String value) {
        this.key = key;
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
        return key + " " + value;

    }
}
