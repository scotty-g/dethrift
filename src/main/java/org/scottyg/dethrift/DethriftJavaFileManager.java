package org.scottyg.dethrift;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

public class DethriftJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private DethriftClassLoader classLoader;

    public DethriftJavaFileManager(JavaFileManager fileManager, DethriftClassLoader classLoader) {
        super(fileManager);
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
