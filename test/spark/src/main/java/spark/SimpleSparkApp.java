package spark;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SimpleSparkApp {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("Simple Spark Example")
                .master("local")
                .enableHiveSupport()
                .getOrCreate();

        Dataset<Row> text = spark.read().format("text").load("spark/src/main/java/spark/SimpleSparkApp.java");
        text.map((MapFunction<Row, String>) value -> value.getString(0), Encoders.STRING()).show();
    }
}
