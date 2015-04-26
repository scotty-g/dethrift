package org.scottyg.dethrift;

import javax.tools.JavaFileManager;
import javax.tools.ToolProvider;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

// TODO need to understand how to control where class files are generated, if needed.
// things could get funky by overwritting existing classes.
// our classloader should protect us
public class JavaCompiler {

    private static javax.tools.JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

    public Class<?> compile(SourceCode sourceCode, DethriftClassLoader classLoader) throws Exception {
        List<SourceCode> compilationUnits = Arrays.asList(sourceCode);
        //DethriftClassLoader classLoader = new DethriftClassLoader(ClassLoader.getSystemClassLoader());
        //DethriftClassLoader classLoader = new DethriftClassLoader(null);
        JavaFileManager fileManager = new JavaFileManagerImpl(
                javac.getStandardFileManager(null, null, StandardCharsets.UTF_8), classLoader);
        javax.tools.JavaCompiler.CompilationTask task =
                javac.getTask(null, fileManager, null, null, null, compilationUnits);
        boolean status = task.call();
        if(!status) {
            throw new RuntimeException("compile failed");
        }

        return classLoader.loadClass(sourceCode.getClassName());
    }
}
