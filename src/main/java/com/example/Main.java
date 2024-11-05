package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

    private static String username;
    
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket("localhost", 3000);
        System.out.println("Il client si è collegato");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
        //input user
        Scanner scanner = new Scanner(System.in);
        
        //Validazione della connessione tramite l'inserimento dell'username
        Input_username name = new Input_username(scanner, in, out);
        username = name.input_username();
        System.out.println("Username: " + username);

        ReceiveThread r = new ReceiveThread(in, out);
        r.start();
    
        sendMessage(in, out, scanner);
            
        //Se per qualche motivo esce dal loop, significa che sta lasciando la chat
        in.close();
        out.close();
        scanner.close();
        socket.close();
            
    }
    
    public static void sendMessage(BufferedReader in, DataOutputStream out, Scanner scan) throws IOException{
        String message;
        // Loop per inviare messaggi
        while (true) { 
            System.out.println("Inserire il messaggio da mandare all'altro utente");
            message = scan.nextLine();
            // Invia il messaggio al server
            out.writeBytes(message + "\n");
    
            // Condizione di uscita
            if (message.equalsIgnoreCase("exit")) {
                System.out.println("Disconnessione...");
                break;
            }
        }
    }
}