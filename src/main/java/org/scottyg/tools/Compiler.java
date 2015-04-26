package org.scottyg.tools;

import org.apache.thrift.TDeserializer;
import org.apache.thrift.protocol.TCompactProtocol;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by deflin39 on 4/16/15.
 */
public class Compiler {

    public static void main(String[] args) {
        try {
            new Compiler().run();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run() throws Exception {
        String fileName = "/home/deflin39/code/thrift/dethrift/src/main/java/org/scottyg/Person.java";
        byte[] code = Files.readAllBytes(Paths.get(fileName));
        String codeContents = new String(code, StandardCharsets.UTF_8);

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileObject obj = new SimpleJavaFileObjectImpl("org.scottyg.tools.Person", codeContents);

        List<JavaFileObject> units = Arrays.asList(obj);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, units);
        boolean result = task.call();
        System.out.println("Compilation succeeded: " + result);

        Person person = (Person) Class.forName("org.scottyg.tools.Person").newInstance();
        fileName = "/home/deflin39/code/thrift/dethrift/person.dat";
        byte[] data = Files.readAllBytes(Paths.get(fileName));
        TDeserializer deserializer = new TDeserializer(new TCompactProtocol.Factory());
        deserializer.deserialize(person, data);
        System.out.println(person);

    }

    public class SimpleJavaFileObjectImpl extends SimpleJavaFileObject {

        private String code;

        public SimpleJavaFileObjectImpl(String fileName, String contents) {
            super(URI.create("string:///" + fileName.replace('.','/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = contents;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}
