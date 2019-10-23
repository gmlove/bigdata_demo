# useful links:
- hadoop web dfs: http://hd01-1:50070/
- yarn application: http://hd01-1:8088/
- yarn default value: https://hadoop.apache.org/docs/r2.9.1/hadoop-yarn/hadoop-yarn-common/yarn-default.xml
- spark common problems: http://whatbeg.com/2017/05/28/sparkerror.html


spark on yarn issues
-------------------------

## spark on yarn提交任务时报ClosedChannelException解决方案
- https://wanggancheng.gitbooks.io/bigdatapractice2017/spark/sparkshi-yong-wen-ti-fen-xi.html
- https://stackoverflow.com/questions/38988941/running-yarn-with-spark-not-working-with-java-8

可能的原因：给节点分配的内存太小，yarn直接kill掉了进程，导致ClosedChannelException

add property to `yarn-site.xml`

```xml
<property>
    <name>yarn.nodemanager.pmem-check-enabled</name>
    <value>false</value>
</property>
<property>
    <name>yarn.nodemanager.vmem-check-enabled</name>
    <value>false</value>
</property>
```

## 通过8088查看Applications任务的logs时报错：Failed while trying to construct...
- http://blog.51yip.com/hadoop/2066.html

add config to `mapred-site.xml`:

```xml
    <property>
        <name>mapreduce.jobhistory.address</name>
        <value>hd01-1:10020</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.webapp.address</name>
        <value>hd01-1:19888</value>
    </property>
```

add property to `yarn-site.xml`

```xml
    <property>
         <name>yarn.log.server.url</name>
         <value>http://hd01-1:19888/jobhistory/logs</value>
    </property>
```


Create and submit application to yarn programmatically
-------------------------

1. map remote yarn port: `ssh -L 8032:hd01-1:8032 root@rhd01-1`
2. map remote hadoop port(may not needed): `ssh -L 9000:hd01-1:9000 root@rhd01-1`
3. build and upload jar: `mvn package && scp yarn/target/hadoop-yarn-1.0-SNAPSHOT.jar rhd01-1:/tmp && ssh rhd01-1 "source ~/dev/projects/hadoop/hadoop/env; hadoop dfs -put -f /tmp/hadoop-yarn-1.0-SNAPSHOT.jar /tmp"`
4. add `core-site.xml` and `yarn-site.xml` to `src/resources/` (change related host to localhost)
5. run locally


===========================
Kerberos 简单配置攻略: https://makeling.github.io/bigdata/72ac84e3.html


Config kerberos with hadoop (能启动，但是外部不好访问容器内部的kdc，待验证功能)

```
# sync configuration
ssh rhd01-6 'yum install krb5-server krb5-libs krb5-workstation -y'
make sync-krb5
for i in 2 3 4 5; do ssh rhd01-$i 'yum -y install krb5-libs krb5-workstation'; done
# init krb server
ssh rhd01-6 'kdb5_util create -r HADOOP.COM -s'  # KDC database master key: 123456
# start krb server
ssh rhd01-6 '/usr/sbin/krb5kdc; /usr/sbin/kadmind;'
# configure nodes dns
for i in 1 2 3 4 5 6; do     hd01_host="`docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' gmliao-host-hd01-$i` hd01-$i.hadoop.com";     hd01_hosts="${hd01_hosts}\n${hd01_host}"; done
for i in 1 2 3 4 5 6; do     docker exec gmliao-host-hd01-$i bash -c "echo -e \"${hd01_hosts}\" >> /etc/hosts"; done
```
kadmin.local addprinc root/admin
kadmin.local addprinc gml/admin
kadmin.local ktadd -k /var/kerberos/krb5kdc/kadm5.keytab gml/admin@HADOOP.COM
kinit -kt /var/kerberos/krb5kdc/kadm5.keytab gml/admin@HADOOP.COM
for i in 1 2 3 4 5; do \
    kadmin.local addprinc -randkey root/hd01-$i@HADOOP.COM; \
    kadmin.local addprinc -randkey HTTP/hd01-$i@HADOOP.COM; \
    mkdir /root/dev/projects/hadoop/data/hd01-$i/conf; \
    kadmin.local xst -k /root/dev/projects/hadoop/data/hd01-$i/conf/hadoop.keytab root/hd01-$i HTTP/hd01-$i; \
done

for i in 1 2 3 4 5; do \
    ssh hd01-$i 'yum install -y apache-commons-daemon-jsvc.x86_64'; \
done


