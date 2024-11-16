package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Input_username {

    //colori e stili per i testi  
    String RED_TEXT = "\033[31m";
    String GREEN_TEXT = "\033[32m";
    String YELLOW_TEXT = "\033[33m";
    String MAGENTA_TEXT = "\033[35m";
    String BLUE_TEXT = "\033[36m";
    String BOLD_Text = "\033[1m";

    String RESET_TEXT = "\033[0m";

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
            case ">" -> System.out.println(RED_TEXT + "Username troppo lungo, RETRY" + RESET_TEXT);
            case "<" -> System.out.println(RED_TEXT + "Username troppo corto, RETRY" + RESET_TEXT);
            case "ERROR_500" -> System.out.println(RED_TEXT + "Internal Server Error" + RESET_TEXT);
            case "ERROR_400" -> System.out.println(RED_TEXT + "Invalid Character present, RETRY" + RESET_TEXT);
            case "ERROR_401" -> System.out.println(RED_TEXT + "Blank Username, RETRY" + RESET_TEXT);
            case "ERROR_402" -> System.out.println(RED_TEXT + "Username already taken, RETRY" + RESET_TEXT);
            case "SUCC_200" -> System.out.println(GREEN_TEXT + BOLD_Text + "Welcome to our team " + this.username + "!!" + RESET_TEXT);
            default -> System.out.println(RED_TEXT + BOLD_Text + " - - ERROR - - " + RESET_TEXT);
        }
    }
}
