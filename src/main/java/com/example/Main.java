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
    //private static String[] group_codes;
    
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
        System.out.println("\n Username: " + username);

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
        //Stampa i comandi eseguibili dall'utente
        print_comands();
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
        }
    }

    public static void print_comands(){
        //Sending a message
        System.out.println("- - TO SEND A MESSAGE (no need for the \"\" when writing the message) - - ");
        System.out.println("@nome_username \"message\" to send a message to user nome_username");
        System.out.println("@All \"message\" to send a message to everyone");
        System.out.println("G@group_name \"message\" to send a message to group group_name");
        //Create group or add user
        System.out.println("- - GROUP CREATION / USER FRIENDSHIP - - ");
        System.out.println("/create_group \"group_name\" to create a group with group_name");
        System.out.println("/add_user \"username\" to add user with username");
        System.out.println("/accept Accept the last friendship request");
        System.out.println("/accept_all Accept all friendship requests");
        System.out.println("/reject Reject the last friendship request");
        System.out.println("/reject_all Reject all friendship requests");
        //Lists
        System.out.println("- - LISTS - - ");
        System.out.println("@_list show all available private chats");
        System.out.println("G@_list show all available groupchats");
        System.out.println("/list_all to show both the available private chats and groupchats");
        System.out.println("Enter EXIT to exit");

    }

    //Simple Chat(Client - Server) Communication, hours spent coding: 8 
}