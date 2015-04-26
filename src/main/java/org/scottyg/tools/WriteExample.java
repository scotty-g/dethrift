package org.scottyg.tools;

import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by deflin39 on 4/16/15.
 */
public class WriteExample {

    public static void main(String[] args) {
        try {
            new WriteExample().run();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run() throws Exception {
        Person p = new Person()
                .setId(3)
                .setFirstName("First")
                .setLastName("Last");

        String fileName = "/home/deflin39/code/thrift/dethrift/personJSON.dat";
        TSerializer serializer = new TSerializer(new TJSONProtocol.Factory());
        byte[] data = serializer.serialize(p);
        Files.write(Paths.get(fileName), data);
    }
}
