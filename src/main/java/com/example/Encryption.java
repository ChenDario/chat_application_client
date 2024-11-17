package com.example;

import java.math.BigInteger;
import java.util.Random;

public class Encryption {
    // Variables to store RSA keys
    private BigInteger e; // Public exponent (part of the public key)
    private BigInteger d; // Private exponent (part of the private key)
    private BigInteger n; // Modulus common to both public and private keys

    // Empty constructor for the Encryption class
    public Encryption() {
    }

    // Method to generate RSA keys
    public void generateKeys(int bitLength) {
        Random rand = new Random();

        // Generate two random prime numbers p and q of the specified bit length
        BigInteger p = BigInteger.probablePrime(bitLength, rand);
        BigInteger q = BigInteger.probablePrime(bitLength, rand);

        // Calculate the modulus n = p * q
        n = p.multiply(q);

        // Calculate φ(n) = (p-1) * (q-1), where φ(n) is Euler's totient function
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Choose e such that it is coprime with φ(n) and less than φ(n)
        do {
            e = new BigInteger(bitLength, rand); // Generate a random number
        } while (!e.gcd(phi).equals(BigInteger.ONE) || e.compareTo(phi) >= 0);

        // Calculate d as the modular multiplicative inverse of e modulo φ(n)
        d = e.modInverse(phi);
    }

    // Encrypt a message using the recipient's public key
    public String encrypt(String message, BigInteger e_dest, BigInteger n_dest) {
        // Convert the plaintext message into a number (byte representation)
        BigInteger messageNumeric = new BigInteger(message.getBytes());

        // Perform RSA encryption: c = m^e mod n
        return messageNumeric.modPow(e_dest, n_dest).toString();
    }

    // Method to decrypt an encrypted message using the private key
    public String decrypt(BigInteger encryptedMessage) {
        // Perform RSA decryption: m = c^d mod n
        BigInteger decryptedNumeric = encryptedMessage.modPow(this.d, this.n);

        // Convert the decrypted number back to a plaintext string
        return new String(decryptedNumeric.toByteArray());
    }

    // Method to get the public key
    public String getPublicKey() {
        return this.e + " : " + this.n;
    }
}
