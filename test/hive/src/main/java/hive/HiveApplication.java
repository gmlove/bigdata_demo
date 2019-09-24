package hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HiveApplication {

	private String driverName = "org.apache.hive.jdbc.HiveDriver";
    private String url = "jdbc:hive2://localhost:10000/default";
    private String user = "root";
    private String password = "";

    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
	  
    public void init() throws Exception {
        Class.forName(driverName);
        conn = DriverManager.getConnection(url,user,password);
        stmt = conn.createStatement();
    }
    
    public void destory() throws Exception {
        if ( rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (conn != null) {
            conn.close();
        }
    }
    
    public static void main(String[] args) throws Exception {
        HiveApplication app = new HiveApplication();
        app.init();
        app.createTable();
        app.showTables();
        app.descTable();
        app.loadData();
        app.selectData();
        app.countData();
        app.dropTable();
        app.destory();
	}
    
    public void createTable() throws Exception {
        String sql = "create table if not exists pokes (foo int, bar string)";
        stmt.execute(sql);
    }

    public void showTables() throws Exception {
        String sql = "show tables";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }

    public void descTable() throws Exception {
        String sql = "desc pokes";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString(1) + "\t" + rs.getString(2));
        }
    }

    public void loadData() throws Exception {
        String filePath = "examples/files/kv1.txt";
        String sql = "load data local inpath '" + filePath + "' overwrite into table pokes";
        stmt.execute(sql);
    }

    public void selectData() throws Exception {
        String sql = "select * from pokes";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString("foo") + "\t\t" + rs.getString("bar"));
        }
    }

    public void countData() throws Exception {
        String sql = "select count(1) from pokes";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getInt(1) );
        }
    }

    public void dropTable() throws Exception {
        String sql = "drop table if exists pokes";
        stmt.execute(sql);
    }
			
}