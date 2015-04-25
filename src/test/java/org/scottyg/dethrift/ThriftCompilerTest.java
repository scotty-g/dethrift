package org.scottyg.dethrift;

import org.apache.thrift.TBase;
import org.junit.Test;
import org.scottyg.Person;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public class ThriftCompilerTest {

    @Test
    public void test() throws Exception {
        URL url = getClass().getClassLoader().getResource("Person.thrift");
        List<SourceCode> sc = new ThriftCompiler(null).compile(url.toURI());
        System.out.println(sc);
    }

    @Test
    public void testWithOptions() throws Exception {
        URL url = getClass().getClassLoader().getResource("Person.thrift");
        List<SourceCode> sc = new ThriftCompiler(null, ThriftCompiler.JavaOptions.JAVA5,
                                 ThriftCompiler.JavaOptions.PRIVATE_MEMBERS)
                .compile(url.toURI());
        System.out.println(sc);
    }

    @Test
    public void testMulti() throws Exception {
        URL url = getClass().getClassLoader().getResource("User.thrift");
        List<SourceCode> sc = new ThriftCompiler(null, ThriftCompiler.JavaOptions.JAVA5,
                ThriftCompiler.JavaOptions.PRIVATE_MEMBERS)
                .compile(url.toURI());
        System.out.println(sc);
        System.out.println(sc.get(0).getClassName());
        System.out.println(sc.get(1).getClassName());
    }

    @Test
    public void testNamespace() throws Exception {
        URL url = getClass().getClassLoader().getResource("Pet.thrift");
        List<SourceCode> sc = new ThriftCompiler(null).compile(url.toURI());
        System.out.println(sc);
        System.out.println(sc.get(0).getClassName());
        //System.out.println(sc.get(0).getCharContent(false));
    }
}
