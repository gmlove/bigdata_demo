package parquet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import java.util.EnumSet;
import java.util.List;

public class YarnApplication {

    public static void main(String[] args) throws Exception{
        Configuration conf = new YarnConfiguration();
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(conf);
        yarnClient.start();
        List<ApplicationReport> applications = yarnClient.getApplications(EnumSet.of(YarnApplicationState.RUNNING, YarnApplicationState.FINISHED));
        System.out.println("ApplicationId ============> "+applications.get(0).getApplicationId());
        System.out.println("name ============> "+applications.get(0).getName());
        System.out.println("queue ============> "+applications.get(0).getQueue());
        System.out.println("queue ============> "+applications.get(0).getUser());
        yarnClient.stop();
    }
}