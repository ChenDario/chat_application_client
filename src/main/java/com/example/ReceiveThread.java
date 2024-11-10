package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ReceiveThread extends Thread{
    private BufferedReader in;
    private DataOutputStream out;
    private HashMap<String, String> group_codes;
    private volatile boolean running = true; 

    public ReceiveThread(BufferedReader in, DataOutputStream out, HashMap<String, String> group_codes){
        this.in = in;
        this.out = out;
        this.group_codes = group_codes;
    }

    @Override
    public void run(){
        //Il Thread deve sempre stare in ascolto per possibili messaggi
        while(running){
            try {
                String message = "";
                while (!message.equals("Exit") || running) {
                    message = in.readLine();
                    status_codes(message, this.in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread(){
        running = false; 
    }

    public void status_codes(String server_response, BufferedReader in){
        try {
            String messaggio = "";

            //Mancano ancora diversi messaggi di errore e risposta
            switch(server_response){
                
                case "RCV_100":
                    messaggio = in.readLine();
                    System.out.println("Message " + messaggio);
                    break;

                case "RCV_200":
                    //Read the group_name and the code to save it in a HashMap
                    String group_name = in.readLine();
                    String group_code = in.readLine();

                    //HashMap contenente il nome del gruppo e il suo relativo codice
                    this.group_codes.put(group_name, group_code);
                    System.out.println("Group Name: " + group_name + " Group Code: " + group_code);
                    break;

                case "CL_200": 
                    System.out.println("Group Created");
                    System.out.println("Enter /join_G@ group_name - username1, username2...... To add the users to the group");
                    break;

                case "SUCC_200": 
                    System.out.println("Richiesta completata con successo");
                    break;
                
                case "SRV_200":
                    messaggio = in.readLine();
                    System.out.println(messaggio);
                    break;

                case "SUCC_201":
                    System.out.println("Messaggio inviato con successo");
                    break;

                case "MENU_200":
                    print_menu(in);
                    break;

                case "GRP_INFO":
                    group_add(in);
                    break;

                case "ERROR_400":
                    System.out.println("Invalid Character present, RETRY");
                    break;

                case "ERROR_403":
                    System.out.println("Utente già presente");
                    break;

                case "ERROR_404":
                    System.out.println("- - ERROR - - REQUEST NOT FOUND");
                    break;
                
                case "ERROR_404_G":
                    System.out.println("Group NOT FOUND");
                    break;
                
                case "ERROR_404_P":
                    System.out.println("User NOT FOUND");
                    break;

                case "ERROR_405":
                    System.out.println("Request Not Found");
                    break;

                case "ERROR_406": 
                    System.out.println("All available user added to the group");
                    break;

                case "ERROR_500":
                    System.out.println("Internal Server Error");
                    break;

                default: 
                    System.out.println("- - ERROR PROCESSING THE RESPONSE FROM THE SERVER - - ");
                    //Sout for debug
                    System.out.println("Server: " + server_response);
                    break;

            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("- - ERROR - - ");
        }
    }

    public void print_menu(BufferedReader in) throws IOException{
        String messaggio = "";
        
        do {
            messaggio = in.readLine();

            if(!messaggio.equals("MENU_300"))
                System.out.println(messaggio);

        } while (!messaggio.equals("MENU_300"));
    }

    //When someone else add you to a group
    public void group_add(BufferedReader in) throws IOException{
        //Read the group_name and the code to save it in a HashMap
        String group_name = in.readLine();
        String group_code = in.readLine();

        //Aggiungi all HashMap il nome del gruppo e il suo relativo codice
        this.group_codes.put(group_name, group_code);

        System.out.println("You have been added to the group: " + group_name);
    }

}
