package Core;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by joh-mue on 13/04/16.
 */
public class JobSequence implements Iterable<Job> {
    ArrayList<Job> jobs;

    public JobSequence() {
        this.jobs = new ArrayList<>();
    }

    public void add(Job job) {
        jobs.add(job);
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    @Override
    public Iterator<Job> iterator() {
        return jobs.iterator();
    }
}
