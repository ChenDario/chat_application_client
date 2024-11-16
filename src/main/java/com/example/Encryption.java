package com.example;

import java.math.BigInteger;
import java.util.Random;

public class Encryption {
    //Crittografia con Algoritmo RSA 
    private BigInteger e; // Esponente pubblico
    private BigInteger d; // Esponente privato
    private BigInteger n; // Modulo

    public void generateKeys(int bitLength) {
        Random rand = new Random();

        // Genera due numeri primi casuali
        BigInteger p = BigInteger.probablePrime(bitLength, rand);
        BigInteger q = BigInteger.probablePrime(bitLength, rand);

        // Calcola n e φ(n)
        n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Scegli e come numero coprimo con φ(n)
        do {
            e = new BigInteger(bitLength, rand);
        } while (!e.gcd(phi).equals(BigInteger.ONE) || e.compareTo(phi) >= 0);

        // Calcola d come inverso moltiplicativo di e mod φ(n)
        d = e.modInverse(phi);
    }

    //Crittografa un messaggio di testo usando la chiave pubblica dell'utente di destinazione
    public String encrypt(String message, BigInteger e_dest, BigInteger n_dest) {
        BigInteger messageNumeric = new BigInteger(message.getBytes());
        return messageNumeric.modPow(e_dest, n_dest).toString(); // Crittografia: c = m^e mod n
    }

    //Decrittografa un messaggio crittografato usando la chiave privata.
    public String decrypt(BigInteger encryptedMessage) {
        BigInteger decryptedNumeric = encryptedMessage.modPow(this.d, this.n); // Decrittografia: m = c^d mod n
        return new String(decryptedNumeric.toByteArray());
    }

    //Get per la chiave pubblica
    public String getPublicKey() {
        return this.e + " : " + this.n;
    }
}
