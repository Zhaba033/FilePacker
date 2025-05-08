package com.mycompany.textfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStore.SecretKeyEntry;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyWorker {
    
    public static boolean generateKey(String keyName, String password) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(null, null);
        
        SecretKeyEntry secretKeyEntry = new SecretKeyEntry(secretKey);
        ProtectionParameter protectionParam = new KeyStore.PasswordProtection(password.toCharArray());
        keyStore.setEntry(keyName, secretKeyEntry, protectionParam);
        
        if (new File(keyName + ".jceks").exists()) {
            return false;
        } else {
            try (FileOutputStream fos = new FileOutputStream(keyName + ".jceks")) {
                keyStore.store(fos, new char[0]);
                return true;
            }
        }
    }

    public static SecretKey getKey(String keyName, String password){
        try {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        try (FileInputStream fis = new FileInputStream(keyName + ".jceks")) {
            keyStore.load(fis, new char[0]);
        }
        SecretKeyEntry secretKeyEntry = (SecretKeyEntry) keyStore.getEntry(keyName,new KeyStore.PasswordProtection(password.toCharArray()));
        return secretKeyEntry.getSecretKey();
        } catch (Exception e) {
            return null;
        }
        
    }
}
