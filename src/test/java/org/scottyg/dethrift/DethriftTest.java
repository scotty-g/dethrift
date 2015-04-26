package org.scottyg.dethrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by deflin39 on 4/26/15.
 */
public class DethriftTest {

    @Test
    public void test() throws Exception {
        URL idl = getClass().getClassLoader().getResource("person.thrift");
        InputStream data = getClass().getClassLoader().getResourceAsStream("person.dat");
        String out = new Dethrift().deserialize(idl, data, new TCompactProtocol.Factory());
        System.out.println(out);
    }

    @Test
    public void testJson() throws Exception {
        URL idl = getClass().getClassLoader().getResource("person.thrift");
        InputStream data = getClass().getClassLoader().getResourceAsStream("personJSON.dat");
        String out = new Dethrift().deserialize(idl, data, new TJSONProtocol.Factory());
        System.out.println(out);
    }
}
