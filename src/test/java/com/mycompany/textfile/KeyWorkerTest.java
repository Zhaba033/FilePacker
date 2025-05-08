package com.mycompany.textfile;

import java.io.File;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KeyWorkerTest {
    @AfterEach()
    void finish() {
        new File("test.jceks").delete();
    }
    
    @Test
    void generate_key() throws Exception {
        Boolean res = KeyWorker.generateKey("test", "");
        Assertions.assertEquals(true, res, "Создание ключа #1");
        Boolean res2 = KeyWorker.generateKey("test", "");
        Assertions.assertEquals(false, res2, "Создание ключа #2");
    }
    
    @Test
    void get_key() throws Exception {
        KeyWorker.generateKey("test", "");
        SecretKey key = KeyWorker.getKey("test", "");
        Assertions.assertEquals(false, key == null, "Получение ключа #1");
        SecretKey keyF = KeyWorker.getKey("test", "1");
        Assertions.assertEquals(true, keyF == null, "Получение ключа #2");
    }
}
