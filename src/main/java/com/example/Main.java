package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    private static String username;
    private static Encryption safe_message = new Encryption();
    private static HashMap<String, String> users_key = new HashMap<>();
    private static HashMap<String, String> group_codes = new HashMap<>();

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        //Codici Gruppo non usati.args..

        Socket socket = new Socket("localhost", 3000);
        System.out.println(ConsoleColors.YELLOW_TEXT + "Il client si è collegato" + ConsoleColors.RESET_TEXT);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // input user
        Scanner scanner = new Scanner(System.in);

        // Validazione della connessione tramite l'inserimento dell'usernamem
        Input_username name = new Input_username(scanner, in, out);
        username = name.input_username();

        //Generate public and private key
        create_keys(safe_message, out);

        // Avvia la ricezione dei messaggi
        ReceiveThread r = new ReceiveThread(in, out, group_codes, safe_message, users_key);
        r.start();

        //Handle The Requests From User
        UserRequestClient.user_input_request(out, scanner, users_key, safe_message, group_codes);

        //Close everything
        in.close();
        out.close();
        scanner.close();
        socket.close();
    }

    private static void create_keys(Encryption safe_message, DataOutputStream out) throws IOException{
        //Generate private and public key
        safe_message.generateKeys(1024);
        String publicKey = safe_message.getPublicKey();

        //Sending public key to the server for the encryption
        out.writeBytes("PublicKey " + publicKey + "\n");
    } 
}