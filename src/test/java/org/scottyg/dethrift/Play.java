package org.scottyg.dethrift;

import org.junit.Test;

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
        javac.compile(sc);

    }
}
