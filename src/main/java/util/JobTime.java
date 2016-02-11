package util;

/**
 * Created by Johannes on 09/02/16.
 */
public class JobTime {
    private String jobName;
    private Long delay;

    public JobTime(String jobName, Long delay) {
        this.jobName = jobName;
        this.delay = delay;
    }

    public String getJobName() {
        return jobName;
    }

    public Long getDelay() {
        return delay;
    }

    @Override
    public String toString() {
        return "JobTime{" +
                "jobName='" + jobName + '\'' +
                ", delay=" + delay +
                '}';
    }
}
