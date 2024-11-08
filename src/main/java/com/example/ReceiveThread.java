package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReceiveThread extends Thread{
    private BufferedReader in;
    private DataOutputStream out;

    public ReceiveThread(BufferedReader in, DataOutputStream out){
        this.in = in;
        this.out = out;
    }

    @Override
    public void run(){
        //Il Thread deve sempre stare in ascolto per possibili messaggi
        while(true){
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    status_codes(message, this.in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                
                case "SUCC_200":
                    messaggio = in.readLine();
                    System.out.println(messaggio);
                    break;

                case "SUCC_201":
                    System.out.println("Messaggio inviato con successo");
                    break;

                case "MENU_200":
                    //Print until he gave the command to stop
                    print_menu(in);
                    break;

                case "ERROR_404":
                    System.out.println("- - ERROR - - REQUEST NOT FOUND");
                    break;
                
                case "ERROR_404_G":
                    System.out.println("Group/s NOT FOUND");
                    break;
                
                case "ERROR_404_P":
                    System.out.println("Private Chat/s NOT FOUND");
                    break;

                case "ERROR_405":
                    System.out.println("Request Not Found");
                    break;

                case "ERROR_500":
                    System.out.println("Internal Server Error");
                    break;

                default: 
                    System.out.println("- - ERROR PROCESSING THE RESPONSE FROM THE SERVER - - ");
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

}
