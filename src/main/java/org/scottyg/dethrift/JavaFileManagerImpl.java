package org.scottyg.dethrift;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

/**
 * Created by deflin39 on 4/19/15.
 */
public class JavaFileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {

    private CompiledCode compiledCode;

    public JavaFileManagerImpl(JavaFileManager fileManager, CompiledCode compiledCode) {
        super(fileManager);
        this.compiledCode = compiledCode;
    }


    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        return compiledCode;
    }
}
