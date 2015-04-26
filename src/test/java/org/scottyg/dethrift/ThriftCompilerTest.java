package org.scottyg.dethrift;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThriftCompilerTest {

    @Test
    public void testSingleStruct() throws Exception {
        String fileName = "person.thrift";
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        List<SourceCode> scs = new ThriftCompiler().compile(fileName, is);
        assertTrue(scs.size() == 1);

        SourceCode sc = scs.get(0);
        assertEquals("Person", sc.getClassName());
        assertTrue(sc.getCharContent(false).toString().contains("public class Person"));
    }

    @Test
    public void testWithOptions() throws Exception {
        String fileName = "person.thrift";
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        List<SourceCode> scs = new ThriftCompiler(ThriftCompiler.JavaOptions.PRIVATE_MEMBERS).compile(fileName, is);
        assertTrue(scs.size() == 1);

        SourceCode sc = scs.get(0);
        assertEquals("Person", sc.getClassName());
        assertTrue(sc.getCharContent(false).toString().contains("private long id"));
    }

    @Test
    public void testMultipleStructures() throws Exception {
        String fileName = "user.thrift";
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        List<SourceCode> scs = new ThriftCompiler(ThriftCompiler.JavaOptions.JAVA5,
                ThriftCompiler.JavaOptions.PRIVATE_MEMBERS)
                .compile(fileName, is);
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
        String fileName = "car.thrift";
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        List<SourceCode> scs = new ThriftCompiler().compile(fileName, is);
        assertTrue(scs.size() == 1);

        SourceCode sc = scs.get(0);
        assertEquals("org.scottyg.Car", sc.getClassName());
        assertTrue(sc.getCharContent(false).toString().contains("public class Car"));
    }
}
