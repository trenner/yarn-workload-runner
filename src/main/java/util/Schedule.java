package util;

import Core.Job;
import Core.JobSequence;

import java.util.ArrayList;

/**
 * Created by joh-mue on 09/02/16.
 */
public class Schedule extends ArrayList<JobSequence> {
    private String experimentName;

    public Schedule(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getExperimentName() {
        return experimentName;
    }
}
