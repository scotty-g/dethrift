package org.scottyg.dethrift;

import org.apache.thrift.TBase;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by deflin39 on 4/18/15.
 */
public class Api {

    public String deserialize(URL idl, byte[] data) {
        return null;
    }

    public OutputStream deserialize(InputStream idl, InputStream data) {
        return null;
    }

    // temp until i figure out the URL stuff
    public String deserialize(File file, byte[] data) {
        return null;
    }

    public TBase<?, ?> compile(URL idl) {
        return null;
    }
}
