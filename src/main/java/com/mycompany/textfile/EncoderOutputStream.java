package com.mycompany.textfile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EncoderOutputStream extends OutputStream {

    int codeValue;
    FileOutputStream fos;

    EncoderOutputStream(FileOutputStream fos, int value) {
        this.fos = fos;
        codeValue = value;
    }

    @Override
    public void write(int b) throws IOException {
        if (b + codeValue > 255) {
            fos.write(b + codeValue - 256);
        } else {
            fos.write(b + codeValue);
        }
    }

}
