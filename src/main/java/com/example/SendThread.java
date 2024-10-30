package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class SendThread extends Thread{
    private BufferedReader in;
    private DataOutputStream out;
    private Scanner scan;

    public SendThread(BufferedReader in, DataOutputStream out, Scanner scan){
        this.in = in;
        this.out = out;
        this.scan = scan;
    }


    @Override
    public void run(){
        String message = "";
        do { 
            try {
                System.out.println("Si prega di inserire un messaggio da mandare all'altro utente"); // TODO: handle exception
                message = scan.nextLine();
                while (message != null) {
                    out.writeBytes(message + "\n");
                }
            } catch (IOException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } while (!message.equals("exit") || !message.isEmpty());
    }

}
