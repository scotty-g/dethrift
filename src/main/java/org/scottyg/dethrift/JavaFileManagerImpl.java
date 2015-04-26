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

    //private CompiledCode compiledCode;
    private DethriftClassLoader classLoader;

    public JavaFileManagerImpl(JavaFileManager fileManager, /*CompiledCode compiledCode, */
                               DethriftClassLoader classLoader) {
        super(fileManager);
        //this.compiledCode = compiledCode;
        //classLoader.addCode(compiledCode);
        this.classLoader = classLoader;
    }


    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className,
                                               JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        try {
            CompiledCode compiledCode = new CompiledCode(className);
            classLoader.addCode(compiledCode);
            return compiledCode;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }
}
