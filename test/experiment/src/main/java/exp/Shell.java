package exp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Shell {

    public static void main(String[] args) throws IOException, InterruptedException {
        Process exec = Runtime.getRuntime().exec("/bin/ls /");
        Thread inputStreamDumper = new Thread(() -> {
            BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            while (true) {
                try {
                    String line;
                    if ((line = br.readLine()) == null) {
                        break;
                    }
                    System.out.println("input stream from command: " + line);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        inputStreamDumper.setDaemon(true);
        inputStreamDumper.start();

        Thread errStreamDumper = new Thread(() -> {
            BufferedReader br = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
            while (true) {
                try {
                    String line;
                    if ((line = br.readLine()) == null) {
                        break;
                    }
                    System.out.println("error stream from command: " + line);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        errStreamDumper.setDaemon(true);
        errStreamDumper.start();

        int exitCode = exec.waitFor();
        System.out.println("command exit with: " + exitCode);
    }
}
