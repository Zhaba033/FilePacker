package com.mycompany.textfile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Encoder {
    int b;
    
    Encoder(int b) {
        this.b = b;
    }
    Encoder() {
        
    }
    
     public void encode(String fileName, int val, boolean delete, String resultFile) {
         File f = new File(fileName);
         if (val != 0) {
             int valueCoding = val;
             try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(fileName), 4096)) {
                 FileOutputStream fos;
                 fos = new FileOutputStream(resultFile);
                 byte[] buf = new byte[b];
                 while (fis.read(buf) != -1) {
                     for (int i = 0; i < buf.length; i++) {
                         if (buf[i] + valueCoding > 255) {
                             fos.write(buf[i] + valueCoding - 256);
                         } else {
                             fos.write(buf[i] + valueCoding);
                         }
                     }
                 }
                 fos.close();
             } catch (IOException e) {
                 System.out.println("Error (enc): " + e.getMessage());
             }
             if (delete) {
                 f.delete();
             }
         }
         else {
             File rf = new File(resultFile);
             f.renameTo(rf);
         }
    }

    public File decode(String fileName, int val, boolean delete) {
        if (val != 0) {
            try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(fileName), 4096)) {
                FileOutputStream fos;
                fos = new FileOutputStream("packed_dec.txt");
                byte[] buf = new byte[b];
                int x;
                while ((x = fis.read(buf)) != -1) {
                    for (byte i : buf) {
                        if (x == 0) {
                            break;
                        }
                        if (i - val < 0) {
                            fos.write(256 + i - val);
                        } else {
                            fos.write(i - val);
                        }
                        x -= 1;
                    }
                }
                fos.close();
            } catch (IOException e) {
                System.out.println("Error (dec): " + e.getMessage());
            }
            File f1 = new File("packed_dec.txt");
            return f1;
        }
        else {
            return null;
        }
    }
}
