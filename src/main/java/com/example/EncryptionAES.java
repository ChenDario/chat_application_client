package com.example;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionAES {

    //Metodo per criptare i dati
    public static String encrypt(String message, String key) throws Exception{
        // Creazione della chiave AES a partire dalla chiave in input
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");

        // Creazione del cipher per AES
        Cipher c = Cipher.getInstance("AES");

        // Inizializzazione del cipher in modalità cifratura
        c.init(Cipher.ENCRYPT_MODE, secretKey);

        // Eseguiamo la cifratura
        byte[] cipher = c.doFinal(message.getBytes()); 

        return Base64.getEncoder().encodeToString(cipher);   
    }

    //Metodo per decifrare i dati
    public static String decrypt(String message, String key) throws Exception {
        // Creazione della chiave AES a partire dalla chiave in input
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");

        // Creazione del cipher per AES
        Cipher cipher = Cipher.getInstance("AES");
        // Inizializzazione del cipher in modalità decifrazione
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Decodifica dei dati cifrati da Base64
        byte[] decodedData = Base64.getDecoder().decode(message);

        // Eseguiamo la decifrazione
        byte[] decryptedData = cipher.doFinal(decodedData);

        return new String(decryptedData);    
    }
}
