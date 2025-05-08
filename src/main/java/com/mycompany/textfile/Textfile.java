package com.mycompany.textfile;

import java.io.Console;
import java.io.File;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Textfile {
    static Packer packer = new Packer(4096);
    
    public static void main(String[] args) {
        startEncryptor(args);
    }
    
    private static void startEncryptor(String[] args) {
        if (args.length == 0) {
            log.info("\nЧтобы использовать программу шифрования, укажите тип задачи: genkey, encrypt, decrypt\nЧтобы получить подробную информацию о каждом типе команды введите help\n");
        } else if (args.length == 1) {
            switch (args[0]) {
                case "genkey":
                    log.info("Использование: genkey KEY_NAME");
                    break;
                case "encrypt":
                    log.info("Использование: encrypt DIR_NAME KEY_NAME");
                    break;
                case "decrypt":
                    log.info("Использование: decrypt DIR_NAME KEY_NAME");
                    break;
                case "help":
                    log.info("Использование: genkey KEY_NAME\nИспользование: encrypt DIR_NAME KEY_NAME\nИспользование: decrypt DIR_NAME KEY_NAME");
                    break;
            }
        } else {
            String opType = args[0];
            String keyName;
            try {
                switch (opType) {
                    case "genkey":
                        keyName = args[1];
                        String passwordGen = generatePassword(true);
                        if (passwordGen != null) {
                            if (!KeyWorker.generateKey(keyName, passwordGen)) {
                                log.error("Ошибка генерации ключа, скорее всего, имя уже занято");
                            }
                        }
                        break;
                    case "encrypt":
                        if (args.length != 3) {
                            log.info("Использование: encrypt DIR_NAME KEY_NAME");
                            break;
                        }
                        keyName = args[2];
                        String passwordEnc = generatePassword(false);
                        if (passwordEnc == null) {
                            break;
                        }
                        SecretKey keyEnc = KeyWorker.getKey(keyName, passwordEnc);
                        String dirNameEnc = args[1];
                        File dirEnc = new File(dirNameEnc);
                        if (dirEnc.exists()) {
                            if (keyEnc != null) {
                                Encryptor enc = new Encryptor();
                                enc.encryptDir(dirEnc, keyEnc);
                            } else {
                                log.error("Неверное название ключа или пароль от него");
                            }
                        } else {
                            log.error("Указанной директории не существует");
                        }
                        break;
                    case "decrypt":
                        if (args.length != 3) {
                            log.info("Использование: decrypt DIR_NAME KEY_NAME");
                            break;
                        }
                        keyName = args[2];
                        String passwordDec = generatePassword(false);
                        if (passwordDec == null) {
                            break;
                        }
                        SecretKey keyDec = KeyWorker.getKey(keyName, passwordDec);
                        String dirNameDec = args[1];
                        File dirDec = new File(dirNameDec);
                        if (dirDec.exists()) {
                            if (keyDec != null) {
                                Encryptor enc = new Encryptor();
                                enc.decryptDir(dirDec, keyDec);
                            } else {
                                log.error("Неверное название ключа или пароль от него");
                            }
                        }
                        else {
                            log.error("Указанной директории не существует");
                        }
                        break;
                }
            } catch (Exception e) {
                log.error("Error\n", e);
            }
        }
    }
    
    private static String generatePassword(boolean repeat) {
        String symb = "abcdefghijklmnopqrstuvwxyz1234567890";
        boolean equalsSymb = true;
        
        Console console = System.console();
        
        boolean finish = false;
        while (!finish) {
            String password = new String(console.readPassword("Придумайте пароль (чтобы отменить, введите 0): "));
            if (password.equals("0")) {
                    //finish = true;
                    break;
            }
            for (String s : password.toLowerCase().split("")) {
                if (!symb.contains(s)) {
                    log.error("Пароль может содержать только символы латинского алфавита (a-z)&(A-Z) и цифры (0-9)");
                    equalsSymb = false;
                    break;
                }
            }
            if (equalsSymb) {
                if (password.isEmpty()) {
                    log.error("Пароль не может быть пустым!");
                } else if (password.length() < 4) {
                    log.error("Пароль должен состоять минимум из 4-х символов!");
                } else if (repeat) {
                    if (console.readPassword("Придумайте пароль: ").equals(password)) {
                        return password;
                    } else {
                        log.error("Пароли не совпадают");
                    }
                } else {
                    return password;
                }
            }
        }
        return null;
    }
}
