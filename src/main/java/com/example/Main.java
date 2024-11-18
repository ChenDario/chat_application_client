package com.example;

//Riproduzione di tracce audio
import java.awt.Toolkit;
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
        try{
        Socket socket = new Socket("localhost", 3000);
        System.out.println(ConsoleColors.YELLOW_TEXT + "Il client si è collegato" + ConsoleColors.RESET_TEXT);
        Toolkit.getDefaultToolkit().beep(); // emette un suono di sistema per comunicare che la connessione è avvenuta

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

        Toolkit.getDefaultToolkit().beep(); // emette un suono di sistema per comunicare che la connessione è stata chiusa
        System.out.println(ConsoleColors.MAGENTA_TEXT + "Disconnessione Terminata!" + ConsoleColors.RESET_TEXT);

        //Close everything
        in.close();
        out.close();
        scanner.close();
        socket.close();
        }catch (IOException e) { //gestisce l'eccezzione che avviene se il server non è attivo
            System.err.println(ConsoleColors.ITALIC + ConsoleColors.RED_TEXT + "----ERROR---- \n Impossibile connettersi al server. Assicurati che sia attivo, o riprova più tardi."+ ConsoleColors.RESET_TEXT) ;
        }
    }

    private static void create_keys(Encryption safe_message, DataOutputStream out) throws IOException{
        //Generate private and public key
        safe_message.generateKeys(1024);
        String publicKey = safe_message.getPublicKey();

        //Sending public key to the server for the encryption
        out.writeBytes("PublicKey " + publicKey + "\n");
    } 
}