package yarn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;

public class Login {

    public interface Task {
        void run() throws Exception;
    }

    public static void loginThenDo(Task task) throws IOException, InterruptedException {
        //        System.setProperty("java.security.krb5.conf", "/Users/gmliao 1/dev/frameworks/hadoop/test/hadoop/src/main/resources/krb5.conf");
        System.setProperty("java.security.krb5.conf", "/etc/krb5.conf");
//        System.setProperty("java.security.krb5.realm", "HADOOP.COM");
//        System.setProperty("java.security.krb5.kdc", "hd01-6.HADOOP.COM:1800");
        System.setProperty("sun.security.krb5.debug", "true");
        System.setProperty("sun.security.spnego.debug", "true");
        System.setProperty("java.net.preferIPv4Stack", "true");
        Configuration conf = new Configuration();
//        conf.addResource(new Path("/Users/gmliao 1/dev/frameworks/hadoop/test/hadoop/src/main/resources/hdfs-site.xml"));
        conf.addResource(new Path("/root/dev/projects/hadoop/test/hadoop/src/main/resources/hdfs-site.xml"));

        UserGroupInformation.setConfiguration(conf);
//        UserGroupInformation.loginUserFromKeytab("test@HADOOP.COM",
//                "/root/dev/projects/hadoop/test/test.keytab");
        UserGroupInformation.loginUserFromKeytab(
                "gml/admin@HADOOP.COM",
//                "/Users/gmliao 1/dev/frameworks/hadoop/test/hadoop/src/main/resources/kadm5.keytab"
                "/var/kerberos/krb5kdc/kadm5.keytab"
        );
        UserGroupInformation.getLoginUser().doAs(new PrivilegedExceptionAction() {
            @Override
            public Object run() throws Exception {
                task.run();
                return null;
            }
        });

    }
}
