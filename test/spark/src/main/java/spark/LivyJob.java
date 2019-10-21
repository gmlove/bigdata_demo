package spark;

import org.apache.livy.Job;
import org.apache.livy.JobContext;

public class LivyJob implements Job<Integer> {

    @Override
    public Integer call(JobContext jobContext) throws Exception {
        return null;
    }
}
