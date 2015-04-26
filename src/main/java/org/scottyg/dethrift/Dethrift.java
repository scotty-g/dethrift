package org.scottyg.dethrift;

import com.google.common.io.ByteStreams;
import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocolFactory;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class Dethrift {

    public String deserialize(URL idl, InputStream data, TProtocolFactory protocolFactory) throws Exception {
        List<SourceCode> scs = new ThriftCompiler().compile(idl);

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        DethriftClassLoader newLoader = new DethriftClassLoader(oldLoader);
        Thread.currentThread().setContextClassLoader(newLoader);

        try {
            Class<?> clazz = new JavaCompiler().compile(scs.get(0), newLoader);
            Object obj = clazz.newInstance();
            TDeserializer deserializer = new TDeserializer(protocolFactory);
            deserializer.deserialize(((TBase<?, ?>) obj), ByteStreams.toByteArray(data));
            return obj.toString();
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }
}
