package org.scottyg.dethrift;

import com.google.common.collect.Maps;

import java.util.Map;

public class DethriftClassLoader extends ClassLoader {

    Map<String, CompiledCode> nameToCode = Maps.newHashMap();

    public DethriftClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void addCode(CompiledCode code) {
        nameToCode.put(code.getName(), code);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        CompiledCode cc = nameToCode.get(name);
        if (cc == null) {
            return super.findClass(name);
        }

        byte[] byteCode = cc.getByteCode();
        return defineClass(name, byteCode, 0, byteCode.length);
    }

}
