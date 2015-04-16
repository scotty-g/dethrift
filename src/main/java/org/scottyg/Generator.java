package org.scottyg;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by deflin39 on 4/15/15.
 */
public class Generator {

    public static void main(String[] args) {
        try {
            new Generator().run();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run() {
        // TODO should use a param builder
        String bin = "/usr/local/bin/thrift";
        String idl = "/home/deflin39/code/thrift/dethrift/Person.thrift";
        String options = "-v -r --gen java:private-members,java5 -out /home/deflin39/code/thrift/dethrift/src/main/java";
        String command = String.format("%s %s %s", bin, options, idl);

        runThriftCompiler(command);
        String contents = fetchJavaFile();
        System.out.println(contents);
    }

    public void runThriftCompiler(String command) {
        try {
            // TODO execute in separate thread with timeout
            // TODO write to /tmp/<uuid>/files
            System.out.println("Executing command: " + command);
            ProcessBuilder builder = new ProcessBuilder(command.split(" "));
            builder.inheritIO();
            Process p = builder.start();
            p.waitFor();
        } catch (IOException |InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String fetchJavaFile() {
        try {
            String fileName = "/home/deflin39/code/thrift/dethrift/src/main/java/org/scottyg/Person.java";
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
