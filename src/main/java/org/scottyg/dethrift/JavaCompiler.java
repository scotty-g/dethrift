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

    public void compile(SourceCode sourceCode) throws Exception {
        List<SourceCode> compilationUnits = Arrays.asList(sourceCode);
        CompiledCode compiledCode = new CompiledCode(sourceCode.getClassName());
        DethriftClassLoader classLoader = new DethriftClassLoader(ClassLoader.getSystemClassLoader());
        JavaFileManager fileManager = new JavaFileManagerImpl(
                javac.getStandardFileManager(null, null, StandardCharsets.UTF_8),
                compiledCode, classLoader);
        javax.tools.JavaCompiler.CompilationTask task =
                javac.getTask(null, fileManager, null, null, null, compilationUnits);
        boolean status = task.call();
        if(!status) {
            throw new RuntimeException("compile failed");
        }
        classLoader.loadClass(sourceCode.getClassName());
    }
}
