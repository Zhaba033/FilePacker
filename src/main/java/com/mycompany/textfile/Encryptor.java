package com.mycompany.textfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class Encryptor {
    
    
    public void encryptDir(File dir, SecretKey key) throws Exception {
        encryptFile(dir, key);
    }
    
    public void decryptDir(File dir, SecretKey key) throws Exception {
        decryptFile(dir, key);
    }

    private void encryptFile(File file, SecretKey key) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        if (!file.isDirectory()) {
            try (FileInputStream fis = new FileInputStream(file); FileOutputStream fos = new FileOutputStream(file.getParentFile().getAbsolutePath() + "/ENCRYPTED_" + file.getName())) {
                byte[] fileBytes = fis.readAllBytes();
                byte[] encryptedFile = cipher.doFinal(fileBytes);
                fos.write(encryptedFile);
            }
            file.delete();
        } else {
            for (File f : file.listFiles()) {
                encryptFile(f, key);
            }
        }
    }
    
    private void decryptFile(File file, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        
        if (!file.isDirectory()) {
            try (FileInputStream fis = new FileInputStream(file); FileOutputStream fos = new FileOutputStream(file.getParentFile().getAbsolutePath() + "/" + file.getName().substring(10))) {
                byte[] fileBytes = fis.readAllBytes();
                byte[] decryptedFile = cipher.doFinal(fileBytes);
                fos.write(decryptedFile);
            }
            file.delete();
        } else {
            for (File f : file.listFiles()) {
                decryptFile(f, key);
            }
        }
    }
}
