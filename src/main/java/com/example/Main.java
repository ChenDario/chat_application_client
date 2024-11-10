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
    
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        HashMap<String, String> group_codes = new HashMap<>();

        Socket socket = new Socket("localhost", 3000);
        System.out.println("Il client si è collegato");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
        //input user
        Scanner scanner = new Scanner(System.in);
        
        //Validazione della connessione tramite l'inserimento dell'usernamem
        Input_username name = new Input_username(scanner, in, out);
        username = name.input_username();
        System.out.println("Username: " + username + "\n");
    
        //Avvia la ricezione dei messaggi 
        ReceiveThread r = new ReceiveThread(in, out, group_codes);
        r.start();

        sendMessage(in, out, scanner);

            
        //Se per qualche motivo esce dal loop, significa che sta lasciando la chat
        in.close();
        out.close();
        scanner.close();
        socket.close();
            
    }
    
    public static void sendMessage(BufferedReader in, DataOutputStream out, Scanner scan) throws IOException, InterruptedException{
        String message;

        System.out.println("Enter /show_command to print all the available commands: ");

        // Loop per inviare messaggi
        while (true) { 
            System.out.println("Enter the command to execute or the message you wish to send: ");
            message = scan.nextLine();
            // Invia il messaggio al server
            out.writeBytes(message + "\n");

            // Condizione di uscita
            if (message.equalsIgnoreCase("exit")) {
                System.out.println("Disconnessione...");
                break;
            }
            //Questo sleep permette di far eseguire la richiesta e farla stampare prima che ricominci il loop (cosa visiva)
            Thread.sleep(1000);
        }
    }

    //Simple Chat(Client - Server) Communication, hours spent coding: 18 (dario)
    //Ore sprecate per fare il debug degli errori per motivi stupidi: 10/18
    //Ore sprecate solo per capire il ragionamento in testa: 6
}