package org.scottyg;

import org.apache.thrift.TDeserializer;
import org.apache.thrift.protocol.TCompactProtocol;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by deflin39 on 4/16/15.
 */
public class ReadExample {

    public static void main(String[] args) {
        try {
            new ReadExample().run();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run() throws Exception {
        String fileName = "/home/deflin39/code/thrift/dethrift/person.dat";
        byte[] data = Files.readAllBytes(Paths.get(fileName));
        Person person = new Person();
        TDeserializer deserializer = new TDeserializer(new TCompactProtocol.Factory());
        deserializer.deserialize(person, data);
        System.out.println(person);
    }
}
