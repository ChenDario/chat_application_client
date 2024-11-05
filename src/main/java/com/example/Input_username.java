package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Input_username {
    Scanner scanner;
    BufferedReader in;
    DataOutputStream out;
    private String username;

    public Input_username(Scanner scanner, BufferedReader in, DataOutputStream out){
        this.scanner = scanner;
    }

    public String input_username() throws IOException{
        boolean valido = true;
        do {
            //Inserimento dell'username
            System.out.println("(Sono consentiti tutti i caratteri dell'alfabeto, che saranno case sensitive, i numeri e i 3 caratteri speciali - . _)\nInserire un'username valido: ");
            username = scanner.nextLine();

            //Manda al server l'username per il controllo 
            out.writeBytes(username + "\n");
            String ans = in.readLine();
            //Se contiene un messaggio di errore allora continua il loop
            valido = ans.contains("ERROR") ? false : true;
            //Stampa a schermo la descrizione del risultato
            username_result(ans);
        } while (!valido);

        return username;
    }

    public void username_result(String ans){
        switch (ans) {
            case "ERROR_500":
                System.out.println("Internal Server Error"); 
                break;
            case "ERROR_400":
                System.out.println("Invalid Character present, RETRY");
                break;
            case "ERROR_402":
                System.out.println("Username already taken, RETRY");
                break;
            case "SUCC_200":
                System.out.println("Welcome " + this.username + "!");
                break;
            default:
                System.out.println(" - - ERROR - - "); 
                break;
        }
    }
}
