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
                    System.out.println("Messaggio ricevuto: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void status_codes(String server_response){
        try {
            //Mancano ancora diversi messaggi di errore
            switch(server_response){
                case "ERROR_404":
                    System.out.println("ERROR - - REQUEST NOT FOUND");
                    break;
                
                case "ERROR_404_G":
                    System.out.println("Group/s NOT FOUND");
                    break;
                
                case "ERROR_404_P":
                    System.out.println("Private Chat/s NOT FOUND");
                    break;

                case "ERROR_405":
                    
                    break;

                case "ERROR_500":
                    
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

}
