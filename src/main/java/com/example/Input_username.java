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
        this.in = in;
        this.out = out;
    }

    public String input_username() throws IOException{
        boolean valido = true;
        String ans;
        do {
            //Inserimento dell'username
            System.out.println("(Sono consentiti tutti i caratteri dell'alfabeto, che saranno case sensitive, i numeri e i 3 caratteri speciali - . _)\nInserire un'username valido: ");
            this.username = scanner.nextLine();

            //Manda al server l'username per il controllo 
            out.writeBytes(this.username + "\n");
            ans = in.readLine();
            //Se contiene un messaggio di errore allora continua il loop
            if(ans.contains("ERROR") || ans.contains(">") || ans.contains("<")){
                valido = false; 
            } else {
                valido = true;
            }
            //Stampa a schermo la descrizione del risultato
            username_result(ans);
        } while (!valido);

        return this.username;
    }

    public void username_result(String ans){
        switch (ans) {
            case ">" -> System.out.println("Username troppo lungo, RETRY");
            case "<" -> System.out.println("Username troppo corto, RETRY");
            case "ERROR_500" -> System.out.println("Internal Server Error");
            case "ERROR_400" -> System.out.println("Invalid Character present, RETRY");
            case "ERROR_401" -> System.out.println("Blank Username, RETRY");
            case "ERROR_402" -> System.out.println("Username already taken, RETRY");
            case "SUCC_200" -> System.out.println("Welcome to our team " + this.username + "!!");
            default -> System.out.println(" - - ERROR - - ");
        }
    }
}
