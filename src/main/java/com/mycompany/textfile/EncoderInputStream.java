package com.mycompany.textfile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class EncoderInputStream extends InputStream {

    int codeValue;
    FileInputStream fis;
    
    EncoderInputStream(FileInputStream fis, int value) {
        this.fis = fis;
        codeValue = value;
    }

    @Override
    public int read() throws IOException {
        int i = fis.read();
        if (i - codeValue < 0) {
            return (256 + i - codeValue);
        } else {
            return (i - codeValue);
        }
    }

}
