package org.scottyg.dethrift;

import com.google.common.io.ByteStreams;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by deflin39 on 4/19/15.
 */
public class Play {

    @Test
    public void testURI_URL_WTF() throws Exception {
        URI uri = null;
        URL url = null;
        File file = null;
        Path path = null;

        URI a = Paths.get("/tmp", "dethrift", "test.txt").toUri();
        URI b = new URL("http://www.google.com/fiber").toURI();

        URI test = b;
        try(InputStream is = test.toURL().openStream()) {
            System.out.println(test);
            System.out.println(test.toURL());
            System.out.println(new String(ByteStreams.toByteArray(is)));
            System.out.println(test.toURL().getFile());
        }



    }
}
