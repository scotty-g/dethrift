package org.scottyg.dethrift;

import com.google.common.collect.Lists;

import javax.tools.JavaFileManager;
import javax.tools.ToolProvider;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class JavaCompiler {

    private static javax.tools.JavaCompiler JAVAC = ToolProvider.getSystemJavaCompiler();

    public Class<?> compile(SourceCode sourceCode, DethriftClassLoader classLoader) throws Exception {
        List<SourceCode> compilationUnits = Lists.newArrayList(sourceCode);
        JavaFileManager fileManager = new DethriftJavaFileManager(
                JAVAC.getStandardFileManager(null, null, StandardCharsets.UTF_8), classLoader);
        javax.tools.JavaCompiler.CompilationTask task =
                JAVAC.getTask(null, fileManager, null, null, null, compilationUnits);
        boolean status = task.call();
        if(!status) {
            throw new IllegalStateException("compile failed");
        }

        return classLoader.loadClass(sourceCode.getClassName());
    }
}
