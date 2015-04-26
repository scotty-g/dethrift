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
    public void test() throws Exception {
        Path rootPath = Paths.get("/tmp/dethrift/f39f8459-8d3f-4b8f-a080-1eee6f911037");
        Path userPath = Paths.get("/tmp/dethrift/3374d49a-b336-41db-8687-87528dfce953/User.java");
        Path personPath = Paths.get("/tmp/dethrift/f39f8459-8d3f-4b8f-a080-1eee6f911037/Person.java");
        Path testPath = Paths.get("/tmp/dethrift/3374d49a-b336-41db-8687-87528dfce953/org/Test.java");
        Path test2Path = Paths.get("/tmp/dethrift/3374d49a-b336-41db-8687-87528dfce953/org/scottyg/Java.java");

        SourceCode sc = new SourceCode(rootPath, personPath);
        JavaCompiler javac = new JavaCompiler();
        javac.compile(sc, null);

    }

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
