package org.scottyg.dethrift;

import com.google.common.io.ByteStreams;
import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.protocol.TCompactProtocol;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Created by deflin39 on 4/25/15.
 */
public class JavaCompilerTest {

    @Test
    public void test() throws Exception {
        String fileName = "person.thrift";
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        List<SourceCode> scs = new ThriftCompiler().compile(fileName, is);
        SourceCode sc = scs.get(0);

        new JavaCompiler().compile(sc, null);
    }

    @Test
    public void testMore() throws Exception {
        String fileName = "person.thrift";
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        List<SourceCode> scs = new ThriftCompiler().compile(fileName, is);
        SourceCode sc = scs.get(0);

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        DethriftClassLoader newLoader = new DethriftClassLoader(oldLoader);

        for (int x = 0; x < 10000; x++) {

            Thread.currentThread().setContextClassLoader(newLoader);

            Class<?> clazz = new JavaCompiler().compile(sc, newLoader);
            Object person = clazz.newInstance();

            InputStream data = getClass().getClassLoader().getResourceAsStream("person.dat");
            TDeserializer deserializer = new TDeserializer(new TCompactProtocol.Factory());
            deserializer.deserialize(((TBase<?, ?>) person), ByteStreams.toByteArray(data));
            System.out.println(String.format("i: %s -> %s", x, person));

        }

        Thread.currentThread().setContextClassLoader(oldLoader);

        Thread.sleep(10000);

    }
}
