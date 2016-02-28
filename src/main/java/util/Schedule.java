package util;

import java.util.ArrayList;

/**
 * Created by Johannes on 09/02/16.
 */
public class Schedule extends ArrayList<JobTime> {
    private String experimentName;

    public Schedule(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getExperimentName() {
        return experimentName;
    }
}
