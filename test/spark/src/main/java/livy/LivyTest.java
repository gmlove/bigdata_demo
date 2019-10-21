package livy;

import org.apache.livy.LivyClient;
import org.apache.livy.LivyClientBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class LivyTest {

    public static void main(String[] args) throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        LivyClient client = new LivyClientBuilder()
                .setURI(new URI("http://localhost:8998"))
                .build();

        try {
            String piJar = "target/hadoop-spark-1.0-SNAPSHOT.jar";
            System.err.printf("Uploading %s to the Spark context...\n", piJar);
            client.uploadJar(new File(piJar)).get();

            int samples = 10000;
            System.err.printf("Running PiJob with %d samples...\n", samples);
            double pi = client.submit(new PiJob(samples)).get();

            System.out.println("Pi is roughly: " + pi);
        } finally {
            client.stop(true);
        }
    }
}
