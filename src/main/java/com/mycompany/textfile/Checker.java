package com.mycompany.textfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
public class Checker{
    
    public void saveHash(String dir, String jsonName) throws Exception {
        File directory = new File(dir);
        ArrayList<File> savingDirectory = new ArrayList<>();
        for (File f : directory.listFiles()) {
            if (!f.isDirectory()) {savingDirectory.add(f);}
        }
        JSONObject hashArr = new JSONObject();
        for (File f : savingDirectory) {
            hashArr.put(f.getName(), getFileHash(f));
        }
        try (FileOutputStream fos = new FileOutputStream(jsonName)) {
            fos.write(hashArr.toString(4).getBytes());
        } catch (IOException e) {
            log.error("ERROR: ", e);
        }
        
    }
    
    public String checkHash(String dir, String jsonName) throws Exception {
        ArrayList<String> wrongHash = new ArrayList<>();
        File directory = new File(dir);
        ArrayList<String> savingDirectory = new ArrayList<>();
        for (File f : directory.listFiles()) {
            if (!f.isDirectory()) {savingDirectory.add(f.getName());}
        }
        try (FileInputStream fis = new FileInputStream(jsonName)) {
            byte[] b = fis.readAllBytes();
            JSONObject savedArr = new JSONObject(new String(b));
            for (String fname : savedArr.keySet()) {
                File f = new File(dir + "\\" + fname);
                if (savingDirectory.contains(fname)) {
                    // (debug) System.out.println(savedArr.get(fname) + " " + getFileHash(f));
                    if (!savedArr.get(fname).equals(getFileHash(f))) {
                        wrongHash.add(fname);
                    }
                } else {
                    wrongHash.add(fname);
                }
            }
        } catch (IOException e) {
            log.error("ERROR: ", e);
        }
        if (!wrongHash.isEmpty()) {
            return "Эти файлы были изменены или удалены: " + wrongHash.toString();
        } else {
            return "Все файлы остались неизменными";
        }
    }
    
    private String getFileHash(File file) throws Exception {
        MessageDigest msg = MessageDigest.getInstance("SHA-1");
        try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
            byte[] b = msg.digest(fis.readAllBytes());
            return Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            log.error("ERROR: ", e);
            return null;
        }
    }
}
