package com.mycompany.textfile;

import java.io.Console;
import java.io.File;
import javax.crypto.SecretKey;

public class Textfile {
    static Packer packer = new Packer(4096);
    public static void main(String[] args) { 
        if (args.length == 0) {
            System.out.println("\nЧтобы использовать программу шифрования, укажите тип задачи: genkey, encrypt, decrypt\nЧтобы получить подробную информацию о каждом типе команды введите help\n");
        } else if (args.length == 1) {
            switch (args[0]) {
                case "genkey":
                    System.out.println("Использование: genkey KEY_NAME");
                    break;
                case "encrypt":
                    System.out.println("Использование: encrypt DIR_NAME KEY_NAME");
                    break;
                case "decrypt":
                    System.out.println("Использование: decrypt DIR_NAME KEY_NAME");
                    break;
                case "help":
                    System.out.println("Использование: genkey KEY_NAME\nИспользование: encrypt DIR_NAME KEY_NAME\nИспользование: decrypt DIR_NAME KEY_NAME");
                    break;
            }
        } else {
            String opType = args[0];
            String keyName;
            Console console = System.console();
            try {
                switch (opType) {
                    case "genkey":
                        keyName = args[1];
                        String passwordGen = new String(console.readPassword("Придумайте пароль: "));
                        if (!new String(console.readPassword("Повторите пароль: ")).equals(passwordGen)) {
                            System.out.println("Пароли не совпадают, попробуйте еще раз.");
                        } else {
                            KeyWorker.generateKey(keyName, passwordGen);
                        }
                        break;
                    case "encrypt":
                        if (args.length != 3) {
                            System.out.println("Использование: encrypt DIR_NAME KEY_NAME");
                            break;
                        }
                        keyName = args[2];
                        String passwordEnc = new String(console.readPassword("Введите пароль от данного ключа: "));
                        SecretKey keyEnc = KeyWorker.getKey(keyName, passwordEnc);
                        String dirNameEnc = args[1];
                        File dirEnc = new File(dirNameEnc);
                        if (dirEnc.exists()) {
                            if (keyEnc != null) {
                                Encryptor enc = new Encryptor();
                                enc.decryptDir(dirEnc, keyEnc);
                            } else {
                                System.out.println("Неверное название ключа или пароль от него");
                            }
                        } else {
                            System.out.println("Указанной директории не существует");
                        }
                        break;
                    case "decrypt":
                        if (args.length != 3) {
                            System.out.println("Использование: decrypt DIR_NAME KEY_NAME");
                            break;
                        }
                        keyName = args[2];
                        String passwordDec = new String(console.readPassword("Введите пароль от данного ключа: "));
                        SecretKey keyDec = KeyWorker.getKey(keyName, passwordDec);
                        String dirNameDec = args[1];
                        File dirDec = new File(dirNameDec);
                        if (dirDec.exists()) {
                            if (keyDec != null) {
                                Encryptor enc = new Encryptor();
                                enc.decryptDir(dirDec, keyDec);
                            } else {
                                System.out.println("Неверное название ключа или пароль от него");
                            }
                        }
                        else {
                            System.out.println("Указанной директории не существует");
                        }
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
