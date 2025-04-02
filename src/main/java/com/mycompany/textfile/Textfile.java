package com.mycompany.textfile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Textfile {
    
    public static void main(String[] args) {
        Packer packer = new Packer(4096);
        //packer.pack("mods", "packed.txt", 20);
        packer.unpack("packed.txt", "unpacked", 20);

    }

}
