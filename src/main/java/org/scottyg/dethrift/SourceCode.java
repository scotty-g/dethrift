package org.scottyg.dethrift;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Source code generated by {@link ThriftCompiler}.
 */
public class SourceCode extends SimpleJavaFileObject {

    private String className;
    private String sourceCode;

    public SourceCode(Path root, Path source) throws IOException {
        super(URI.create("string:///" + root.relativize(source)), Kind.SOURCE);
        String nameWithExtension = root.relativize(source).toString().replace('/', '.');
        className = nameWithExtension.substring(0, nameWithExtension.lastIndexOf('.'));
        sourceCode = new String(Files.readAllBytes(source), StandardCharsets.UTF_8);
    }

    public String getClassName() {
        return className;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return sourceCode;
    }
}
