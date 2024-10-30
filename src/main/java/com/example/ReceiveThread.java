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

}
