package org.scottyg.dethrift;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public class ThriftCompilerTest {

    @Test
    public void test() throws Exception {
        URL url = getClass().getClassLoader().getResource("Person.thrift");
        List<Path> paths = new ThriftCompiler(null).compile(url.toURI());
        System.out.println(paths);
    }

    @Test
    public void testWithOptions() throws Exception {
        URL url = getClass().getClassLoader().getResource("Person.thrift");
        List<Path> paths = new ThriftCompiler(null, ThriftCompiler.JavaOptions.JAVA5,
                                 ThriftCompiler.JavaOptions.PRIVATE_MEMBERS)
                .compile(url.toURI());
        System.out.println(paths);
    }

    @Test
    public void testMulti() throws Exception {
        URL url = getClass().getClassLoader().getResource("User.thrift");
        List<Path> paths = new ThriftCompiler(null, ThriftCompiler.JavaOptions.JAVA5,
                ThriftCompiler.JavaOptions.PRIVATE_MEMBERS)
                .compile(url.toURI());
        System.out.println(paths);
    }
}
