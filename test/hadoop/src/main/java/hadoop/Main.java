package hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.setProperty("java.security.krb5.conf", "/Users/gmliao 1/dev/frameworks/hadoop/test/hadoop/src/main/resources/krb5.conf");
//        System.setProperty("java.security.krb5.conf", "/etc/krb5.conf");
//        System.setProperty("java.security.krb5.realm", "HADOOP.COM");
//        System.setProperty("java.security.krb5.kdc", "hd01-6.HADOOP.COM:1800");
        System.setProperty("sun.security.krb5.debug", "true");
        System.setProperty("sun.security.spnego.debug", "true");
        System.setProperty("java.net.preferIPv4Stack", "true");
        Configuration conf = new Configuration();
        conf.addResource(new Path("/Users/gmliao 1/dev/frameworks/hadoop/test/hadoop/src/main/resources/hdfs-site.xml"));
//        conf.addResource(new Path("/root/dev/dev/projects/hadoop/test/hadoop/src/main/resources/hdfs-site.xml"));

        UserGroupInformation.setConfiguration(conf);
//        UserGroupInformation.loginUserFromKeytab("test@HADOOP.COM",
//                "/root/dev/projects/hadoop/test/test.keytab");
        UserGroupInformation.loginUserFromKeytab(
                "gml/admin@HADOOP.COM",
                "/Users/gmliao 1/dev/frameworks/hadoop/test/hadoop/src/main/resources/kadm5.keytab"
//                "/var/kerberos/krb5kdc/kadm5.keytab"
        );

        FileSystem fileSystem = FileSystem.get(conf);
        Path path = new Path("/user/root/input/core-site.xml");
        if (!fileSystem.exists(path)) {
            System.out.println("File does not exists");
            return;
        }
        System.out.println("start read core-site.xml");
        FSDataInputStream in = fileSystem.open(path);
        int numBytes;
        byte[] b = new byte[1024];
        while ((numBytes = in.read(b)) > 0) {
            System.out.println("read core-site.xml: " + (char)numBytes);// code to manipulate the data which is read
        }
        in.close();
        fileSystem.close();
    }
}
