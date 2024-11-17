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
            System.out.println("SCEGLI UN'USERNAME \n (Sono consentiti tutti i caratteri dell'alfabeto, che saranno case sensitive, i numeri e i 3 caratteri speciali - . _)\nInserire un'username valido: ");
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
            case ">" -> System.out.println(ConsoleColors.RED_TEXT + "Username troppo lungo, RETRY" + ConsoleColors.RESET_TEXT);
            case "<" -> System.out.println(ConsoleColors.RED_TEXT + "Username troppo corto, RETRY" + ConsoleColors.RESET_TEXT);
            case "ERROR_500" -> System.out.println(ConsoleColors.RED_TEXT + "Internal Server Error" + ConsoleColors.RESET_TEXT);
            case "ERROR_400" -> System.out.println(ConsoleColors.RED_TEXT + "Invalid Character present, RETRY" + ConsoleColors.RESET_TEXT);
            case "ERROR_401" -> System.out.println(ConsoleColors.RED_TEXT + "Blank Username, RETRY" + ConsoleColors.RESET_TEXT);
            case "ERROR_402" -> System.out.println(ConsoleColors.RED_TEXT + "Username already taken, RETRY" + ConsoleColors.RESET_TEXT);
            case "SUCC_200" -> System.out.println(ConsoleColors.GREEN_TEXT + ConsoleColors.BOLD_TEXT + "Welcome to our team " + this.username + "!!" + ConsoleColors.RESET_TEXT);
            default -> System.out.println(ConsoleColors.RED_TEXT + ConsoleColors.BOLD_TEXT + " - - ERROR - - " + ConsoleColors.RESET_TEXT);
        }
    }
}
