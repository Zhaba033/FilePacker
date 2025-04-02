package com.mycompany.textfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Packer {
    private boolean minimize = true;
    private int b;
    
    Encoder encoder;
    
    
    Packer(int b) {
        this.b = b;
        encoder = new Encoder(b);
    }
    
    
    void minimize(boolean a) {
        minimize = a;
    }
    
    public void pack(String folderName, String resultFile, int value) {
//        String file_noformat_name = "packed_noformat.zxc";
        
        File f = new File(folderName);
        ArrayList<File> fileList = new ArrayList<>();
        int count = 0;
        for (File l : f.listFiles()) {
            if (!l.isDirectory()) {
                fileList.add(l);
                count++;
            }
        }
        try (DataOutputStream dos = new DataOutputStream(new EncoderOutputStream(new FileOutputStream(resultFile),  value))) {
            dos.writeInt(count); // count files (1 string)
            for (File ff : fileList) {
                FileInputStream fis =  new FileInputStream(f.getName() + "/" + ff.getName());
                dos.writeInt(ff.getName().length()); // length of file name
                dos.writeBytes(ff.getName()); // file name
                dos.writeLong(ff.length()); // length of file
                byte[] buf = new byte[b];
                int i, wr;
                wr = 0;
                while ((i = fis.read(buf)) != -1) {
                    if (ff.length()-wr<b) {
                        byte[] buf1 = new byte[(int) ff.length()-wr];
                        System.arraycopy(buf, 0, buf1, 0, (int) ff.length()-wr); //new byte[(int) ff.length()-wr];
                        //System.out.println(Arrays.toString(buf1));
                        dos.write(buf1);
                        break;
                    }
                    //System.out.println(Arrays.toString(buf));
                    dos.write(buf);
                    wr += i;
                }
                fis.close();
            }
        } catch (IOException e) {
            System.out.println("Error (pack): " + e.getMessage());
        }
//        encoder.encode(file_noformat_name, value, minimize, resultFile);
    }
    
    public void unpack(String packFile, String resultFolder, int value) {
        //File f = new File(packFile);
        new File(resultFolder).mkdirs();
        
        //File df = encoder.decode(packFile, value, minimize);
        
        try (DataInputStream dis = new DataInputStream(new EncoderInputStream(new FileInputStream(packFile), value))) {
            int count = dis.readInt(); // count files
            for (int i = 0; i < count; i++) {
                int nameLength = dis.readInt();
                byte[] nameBytes = new byte[nameLength];
                dis.read(nameBytes);
                String name = new String(nameBytes);
                long fileLength = dis.readLong();
                byte[] buf = new byte[b];
                int wr;
                FileOutputStream fos = new FileOutputStream(resultFolder + "/" + name);
                for (int j = 0; j < fileLength; j += wr) {
                    if (fileLength - j < b) {
                        int res = (int) fileLength - j;
                        byte[] buf1 = new byte[res];
                        System.arraycopy(buf, 0, buf1, 0, res);
                        dis.read(buf1);
                        fos.write(buf1);
                        break;
                    }
                    wr = dis.read(buf);
                    fos.write(buf);
                }
                fos.close();
                
            }
        } catch (IOException e) {
            System.out.println("Error (unpack): " + e.getMessage());
        }
    }
}
