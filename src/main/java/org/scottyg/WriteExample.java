package org.scottyg;

import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TCompactProtocol;

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
                .setId(2)
                .setFirstName("scott")
                .setLastName("grissom");

        String fileName = "/home/deflin39/code/thrift/dethrift/person.dat";
        TSerializer serializer = new TSerializer(new TCompactProtocol.Factory());
        byte[] data = serializer.serialize(p);
        Files.write(Paths.get(fileName), data);
    }
}
