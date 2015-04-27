package org.scottyg.dethrift;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ThriftCompilerTest {

    @Test
    public void testSingleStruct() throws Exception {
        URL idl = getClass().getClassLoader().getResource("person.thrift");
        List<SourceCode> scs = new ThriftCompiler().compile(idl);
        assertTrue(scs.size() == 1);

        SourceCode sc = scs.get(0);
        assertEquals("Person", sc.getClassName());
        assertTrue(sc.getCharContent(false).toString().contains("public class Person"));
    }

    @Test
    public void testWithOptions() throws Exception {
        URL idl = getClass().getClassLoader().getResource("person.thrift");
        List<SourceCode> scs = new ThriftCompiler(ThriftCompiler.JavaOptions.PRIVATE_MEMBERS).compile(idl);
        assertTrue(scs.size() == 1);

        SourceCode sc = scs.get(0);
        assertEquals("Person", sc.getClassName());
        assertTrue(sc.getCharContent(false).toString().contains("private long id"));
    }

    @Test
    public void testMultipleStructures() throws Exception {
        URL idl = getClass().getClassLoader().getResource("user.thrift");
        List<SourceCode> scs = new ThriftCompiler(ThriftCompiler.JavaOptions.JAVA5,
                ThriftCompiler.JavaOptions.PRIVATE_MEMBERS)
                .compile(idl);
        assertTrue(scs.size() == 2);

        Map<String, SourceCode> classNameToSourceCode = Maps.uniqueIndex(scs, new Function<SourceCode, String>() {
            @Override
            public String apply(SourceCode input) {
                return input.getClassName();
            }
        });

        SourceCode userSc = classNameToSourceCode.get("User");
        assertTrue(userSc.getCharContent(false).toString().contains("public class User"));

        SourceCode nameSc = classNameToSourceCode.get("Name");
        assertTrue(nameSc.getCharContent(false).toString().contains("public class Name"));
    }

    @Test
    public void testNamespace() throws Exception {
        URL idl = getClass().getClassLoader().getResource("car.thrift");
        List<SourceCode> scs = new ThriftCompiler().compile(idl);
        assertTrue(scs.size() == 1);

        SourceCode sc = scs.get(0);
        assertEquals("org.scottyg.Car", sc.getClassName());
        assertTrue(sc.getCharContent(false).toString().contains("public class Car"));
    }

    @Test
    public void testRemoteIDL() throws Exception {
        URL idl = new URL("https://raw.githubusercontent.com/apache/storm/v0.9.4/storm-core/src/storm.thrift");
        List<SourceCode> scs = new ThriftCompiler().compile(idl);
        assertTrue(scs.size() == 36);
    }

    @Test
    public void testCache() throws Exception {
        URL idl = getClass().getClassLoader().getResource("person.thrift");
        ThriftCompiler compiler = new ThriftCompiler();
        List<SourceCode> scs = compiler.compile(idl);

        // verify SourceCode is cached
        for(int x = 0; x < 100; x++) {
            List<SourceCode> cached = compiler.compile(idl);
            assertSame(scs, cached);
        }
    }

    @Test
    public void testCacheRemoval() throws Exception {
        URL idl = getClass().getClassLoader().getResource("person.thrift");
        ThriftCompiler compiler = new ThriftCompiler();
        List<SourceCode> sc = compiler.compile(idl);
        compiler.reset(idl);
        assertNotSame(sc, compiler.compile(idl));
    }

    @Test
    public void testCacheRemovalAll() throws Exception {
        URL personIDL = getClass().getClassLoader().getResource("person.thrift");
        URL userIDL = getClass().getClassLoader().getResource("user.thrift");
        ThriftCompiler compiler = new ThriftCompiler();
        List<SourceCode> pSc = compiler.compile(personIDL);
        List<SourceCode> uSc = compiler.compile(userIDL);
        compiler.resetAll();
        assertNotSame(pSc, compiler.compile(personIDL));
        assertNotSame(uSc, compiler.compile(userIDL));
    }
}
