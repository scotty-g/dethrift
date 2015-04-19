package org.scottyg.dethrift;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ThriftCompiler {

    private static String thriftBin = "/usr/local/bin/thrift";
    private JavaOptions[] opts;
    private String out;

    public ThriftCompiler(String out, JavaOptions... opts) {
        this.out = out;
        this.opts = opts;
    }

    public List<Path> compile(URI idl) {
        return compile(Paths.get(idl.getPath()));
    }

    public List<Path> compile(Path idl) {
        String bin = getThrift();
        // TODO verify idl...how?

        try {
            if(out == null) {
                String ioTempDir = System.getProperty("java.io.tmpdir");
                String baseDir = "dethrift";
                String uuid = UUID.randomUUID().toString();
                out = Files.createDirectories(Paths.get(ioTempDir, baseDir, uuid)).toString();
            }

            // transform the enums into string options
            List<String> optValues = Lists.transform(Arrays.asList(opts), new Function<JavaOptions, String>() {
                public String apply(JavaOptions input) {
                    return input.get();
                }
            });

            String optString = Joiner.on(',').skipNulls().join(optValues);
            optString = optString.isEmpty() ? optString : (':' + optString);

            // run thrift
            String options = String.format("-v -r --gen java%s -out %s", optString, out);
            String command = String.format("%s %s %s", bin, options, idl.toAbsolutePath().toString());
            execute(command);

            // return files
            final List<Path> paths = Lists.newArrayList();
            Files.walkFileTree(Paths.get(out), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException
                {
                    Objects.requireNonNull(file);
                    Objects.requireNonNull(attrs);
                    if(file.toString().toLowerCase().endsWith(".java")) {
                        paths.add(file);
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
            return paths;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getThrift() {
        if(Files.notExists(Paths.get(thriftBin))) {
            throw new IllegalStateException("Cannot find Thrift binary");
        }

        return thriftBin;
    }

    private void execute(final String command) {
        try {
            System.out.println("Executing command: " + command);
            Executors.newSingleThreadExecutor().submit(new Runnable() {
                public void run() {
                    ProcessBuilder builder = new ProcessBuilder(command.split(" "));
                    builder.inheritIO();
                    try {
                        Process p = builder.start();
                        p.waitFor();
                        if (p.exitValue() != 0) {
                            throw new IllegalStateException("thrift compiler failed");
                        }
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).get(30, TimeUnit.SECONDS);

        } catch (InterruptedException|ExecutionException|TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public enum JavaOptions {
        BEANS("beans"),
        PRIVATE_MEMBERS("private-members"),
        NO_CAMEL("nocamel"),
        FULL_CAMEL("fullcamel"),
        ANDROID("android"),
        ANDROID_LEGACY("android_legacy"),
        JAVA5("java5"),
        REUSE_OBJECTS("reuse-objects"),
        SORTED_CONTAINERS("sorted_containers");

        private String opt;

        JavaOptions(String opt) {
            this.opt = opt;
        }

        public String get() { return opt; }
    }
}
