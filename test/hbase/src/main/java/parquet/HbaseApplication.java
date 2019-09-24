package parquet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell.Type;
import org.apache.hadoop.hbase.CellBuilderFactory;
import org.apache.hadoop.hbase.CellBuilderType;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Arrays;

public class HbaseApplication {

    public static void main(String[] args) throws Exception {
        Logger.getLogger("org.apache.zookeeper").setLevel(Level.DEBUG);
        Logger.getLogger("org.apache.hadoop.hbase.zookeeper").setLevel(Level.DEBUG);
        Logger.getLogger("org.apache.hadoop.hbase.client").setLevel(Level.DEBUG);
        Logger.getRootLogger().setLevel(Level.DEBUG);

        Configuration config = HBaseConfiguration.create();

        String path = HbaseApplication.class
                .getClassLoader()
                .getResource("hbase-site.xml")
                .getPath();
        config.addResource(new Path(path));

        HBaseAdmin.available(config);

        TableName table1Name = TableName.valueOf("Table1");
        String family1 = "Family1";
        String family2 = "Family2";

        Connection connection = ConnectionFactory.createConnection(config);
        Admin admin = connection.getAdmin();
        Table table1 = connection.getTable(table1Name);

        if (!admin.tableExists(table1Name)) {
            TableDescriptor desc = TableDescriptorBuilder.newBuilder(table1Name)
                    .setColumnFamilies(Arrays.asList(ColumnFamilyDescriptorBuilder.of(family1), ColumnFamilyDescriptorBuilder.of(family2))).build();
            admin.createTable(desc);
        }

        byte[] row1 = Bytes.toBytes("row1");
        Put p = new Put(row1);

        String qualifier1 = "Qualifier1";
        p.add(CellBuilderFactory.create(CellBuilderType.SHALLOW_COPY)
                .setRow("row1".getBytes()).setFamily(family1.getBytes()).setQualifier(qualifier1.getBytes())
                .setValue("cell_data".getBytes()).setType(Type.Put)
                .build());
        table1.put(p);

        Get g = new Get(row1);
        Result r = table1.get(g);
        byte[] value = r.getValue(family1.getBytes(), qualifier1.getBytes());

        System.out.println(Bytes.toString(value));
        connection.close();
	}
    
}