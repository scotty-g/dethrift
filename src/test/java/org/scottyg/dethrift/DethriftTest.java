package org.scottyg.dethrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by deflin39 on 4/26/15.
 */
public class DethriftTest {

    @Test
    public void test() throws Exception {
        String fileName = "person.thrift";
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        InputStream data = getClass().getClassLoader().getResourceAsStream("person.dat");
        String out = new Dethrift().deserialize(fileName, is, data, new TCompactProtocol.Factory());
        System.out.println(out);
    }

    @Test
    public void testJson() throws Exception {
        String fileName = "person.thrift";
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        InputStream data = getClass().getClassLoader().getResourceAsStream("personJSON.dat");
        String out = new Dethrift().deserialize(fileName, is, data, new TJSONProtocol.Factory());
        System.out.println(out);
    }

}
