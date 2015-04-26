package org.scottyg.dethrift;

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

        new JavaCompiler().compile(sc);


    }
}
