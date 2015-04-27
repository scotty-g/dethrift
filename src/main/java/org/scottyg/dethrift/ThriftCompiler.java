package org.scottyg.dethrift;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ThriftCompiler {

    private static String thriftBin = System.getProperty("thrift.compiler.bin", "/usr/local/bin/thrift");

    private final JavaOptions[] opts;
    private final ConcurrentHashMap<URL, List<SourceCode>> cache;

    public ThriftCompiler(JavaOptions... opts) {
        this.opts = opts;
        this.cache = new ConcurrentHashMap<>(1000);
    }

    public boolean reset(URL idl) {
        return (cache.remove(idl)) != null;
    }

    public void resetAll() {
        cache.clear();
    }

    public List<SourceCode> compile(URL idl) {
        List<SourceCode> sc = cache.get(idl);
        if(sc != null) {
            return sc;
        }

        String bin = getThrift();
        TempDataPaths dataPaths = new TempDataPaths();
        String idlName = FilenameUtils.getName(idl.toString());

        try {
            // write the idl to the temp data location
            // TODO when we have more than one idl this won't work. we will need to namespace
            Path idlPath = Paths.get(dataPaths.in().toString(), idlName);
            try(InputStream is = idl.openStream()) {
                Files.copy(is, idlPath);
            }

            // transform the option enums into string options
            List<String> optValues = Lists.transform(Arrays.asList(opts), new Function<JavaOptions, String>() {
                public String apply(JavaOptions input) {
                    return input.get();
                }
            });
            String optString = Joiner.on(',').skipNulls().join(optValues);
            optString = optString.isEmpty() ? optString : (':' + optString);

            // run thrift
            String options = String.format("-v -r --gen java%s -out %s", optString, dataPaths.out().toAbsolutePath());
            String command = String.format("%s %s %s", bin, options, idlPath);
            execute(command);

            // return files
            final List<Path> paths = Lists.newArrayList();
            Files.walkFileTree(dataPaths.out(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    Objects.requireNonNull(file);
                    Objects.requireNonNull(attrs);
                    if (file.toString().toLowerCase().endsWith(".java")) {
                        paths.add(file);
                    }

                    return FileVisitResult.CONTINUE;
                }
            });

            sc =  toSourceCode(dataPaths.out(), paths);
            cache.put(idl, sc);
            return sc;
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

    private List<SourceCode> toSourceCode(Path base, List<Path> source) throws IOException {
        List<SourceCode> sourceCode = Lists.newArrayList();
        for(Path p : source) {
            sourceCode.add(new SourceCode(base, p));
        }

        return sourceCode;
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

    private static class TempDataPaths {
        private static final String ioTempDir = System.getProperty("java.io.tmpdir");
        private static final String baseDir = "dethrift";
        private final String uuid;
        private final Path in;
        private final Path out;

        public TempDataPaths() {
            uuid = UUID.randomUUID().toString();
            in = create("in");
            out = create("out");
        }

        public Path in() {
            return in;
        }

        public Path out() {
            return out;
        }

        private Path create(String type) {
            // /tmp/dethrift/<uuid>/[in|out]
            try {
                return Files.createDirectories(Paths.get(ioTempDir, baseDir, uuid, type));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
