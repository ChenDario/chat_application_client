package com.example;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;

public class UserRequestClient {

    public static void user_input_request(DataOutputStream out, Scanner scan, HashMap<String, String> users_key,Encryption safe_message, HashMap<String, String> group_codes) throws IOException, InterruptedException {
        Thread.sleep(500);
        String message = "";
        // Loop per inviare messaggi
        while (true) {
            stampaMenu();

            do {
                String scelta = "";
                //Inserimento dell'operazione
                System.out.println("Scrivi il numero corrispondente all' azione che vorresti fare");
                scelta = scan.nextLine();

                message = handleRequest(scelta, scan, out, users_key, safe_message);
            } while (message == null);// in caso inserisca una stringa non valida ripetere il ciclo

            // Condizione di uscita
            if (message.equalsIgnoreCase("exit")) {
                System.out.println(ConsoleColors.YELLOW_TEXT + "Disconnessione..." + ConsoleColors.RESET_TEXT);
                break;
            }
            // Invia il messaggio al server
            out.writeBytes(message + "\n");

            // Questo sleep permette di far eseguire la richiesta e farla stampare prima che
            // ricominci il loop (cosa visiva)
            Thread.sleep(1000);
        }
    }

    private static String handleRequest(String scelta, Scanner scanner, DataOutputStream out, HashMap<String, String> users_key, Encryption safe_message) throws IOException {
        switch (scelta) {
            case "0":
                return "exit";
            case "1":
                return privateMessage(scanner, users_key, out, safe_message);
            case "2":
                return groupMessage(scanner);
            case "3":
                return broadcastMessage(scanner);
            case "4":
                return "@_list";
            case "5":
                return "G@_list";
            case "6"://Da request not found
                return getUsersInGroup(scanner);
            case "7":
                return "/list_all";
            case "8":
                return create_group(scanner);
            case "9":
                return addUsersGroup(scanner);
            case "10":
                return leftGroup(scanner);
            default:
                System.out.println(ConsoleColors.RED_TEXT + "Request Not Found" + ConsoleColors.RESET_TEXT);
                return null;
        }
    }

    private static String leftGroup(Scanner scanner){
        System.out.println("Inserisci il nome del gruppo da qui vuoi uscire");
        String nomeEliminabile = scanner.nextLine();
        return "/left_G@ " + nomeEliminabile;
    }

    private static String addUsersGroup(Scanner scanner){
        String message = "/join_G@ ";
        System.out.println("Inserisci il nome del gruppo da qui vuoi aggiungere un altro utente");
        String nomeGruppoEsistente = scanner.nextLine();
        message += nomeGruppoEsistente + " - ";
        String rispostaSi;

        do {
            System.out.println("Inserisci il nome utente da inserire");
            String nomeUtente = scanner.nextLine();
            message += nomeUtente + ", "; //aggancia ogni volta utente nuovo
            System.out.println("Vuoi inserire un altro utente? digitare 'si' se la risposta è affermativa");
            rispostaSi = scanner.nextLine();
        } while (rispostaSi.equalsIgnoreCase("si"));

        if (message.length() > 0 && message.charAt(message.length() - 2) == ',') { 
            //togliere l'ultimo carrattere perchè è una ,
            message = message.substring(0, message.length() - 2);
        } //dovrebbe essere a posto

        return message;
    }

    private static String create_group(Scanner scanner){
        System.out.println("Digitare il nome del gruppo da creare");
        String nomeGruppoDaCreare = scanner.nextLine();
        return "/create_group " + nomeGruppoDaCreare;
    }

    private static String getUsersInGroup(Scanner scanner){
        System.out.println("Digita il nome del gruppo per vedere i suoi partecipanti");
        String gruppoEsistente = scanner.nextLine();
        return "/users_group " + gruppoEsistente;
    }    

    private static String broadcastMessage(Scanner scanner){
        System.out.println("Scrivi il messaggio da inviare a tutti: ");
        String messaggioTutti = scanner.nextLine();
        return "@All " + messaggioTutti;
    }

    private static String privateMessage(Scanner scanner, HashMap<String, String> users_key, DataOutputStream out, Encryption safe_message) throws IOException {
        System.out.println("Scrivi il nome utente a cui inviare il messaggio:");
        String nomeUtenteEsistente = scanner.nextLine();
        findPublicKey(nomeUtenteEsistente, users_key, out);

        System.out.println("Scrivi il messaggio:");
        String messaggio = scanner.nextLine();

        return "@" + nomeUtenteEsistente + " " + encrypt_message(messaggio, nomeUtenteEsistente, users_key, safe_message);    
    }

    private static void findPublicKey(String user, HashMap<String, String> users_key, DataOutputStream out) throws IOException{
        if(users_key.get(user) == null)
        out.writeBytes("/request_key " + user + "\n");
        //Se non ha la chiave la richiede al server
    }

    private static String encrypt_message(String message, String user, HashMap<String, String> users_key, Encryption safe_message){
        String public_key = users_key.get(user);
        if (public_key == null) {
            System.out.println(ConsoleColors.RED_TEXT + "Chiave pubblica non trovata per l'utente " + user + ConsoleColors.RESET_TEXT);
            return null;
        }
        String[] key_dest = public_key.split(" : ");
        try {
            return safe_message.encrypt(message, new BigInteger(key_dest[0]), new BigInteger(key_dest[1]));
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED_TEXT + "Errore durante la crittografia del messaggio!" + ConsoleColors.RESET_TEXT);
            return null;
        }
    }

    private static String groupMessage(Scanner scanner) {
        System.out.println("Scrivi il nome del gruppo a cui inviare il messaggio:");
        String nomeGruppo = scanner.nextLine();
        System.out.println("Scrivi il messaggio:");
        String messaggio = scanner.nextLine();
        return "G@" + nomeGruppo + " " + messaggio;
    }

    // funzione per stampare il menu
    private static void stampaMenu() { 
        // tutti print out con le opzione
        System.out.println("0) Uscire dall' aplicazione"); // exit
        System.out.println("1) Inviare un messaggio ad un altro utente"); // @username “message” 
        System.out.println("2) Inviare un messaggio ad un gruppo"); // G@group_name “message”
        System.out.println("3) Inviare un messaggio a tutti gli utenti registrati"); // @All “message”
        System.out.println("4) Lista degli utenti attivi"); // @_list
        System.out.println("5) lista dei gruppi attivi"); // G@_list
        System.out.println("6) Lista dei membri di un gruppo"); // /users_group nome_gruppo
        System.out.println("7) Lista di tutte le chat attive (sia gruppi e sia private)"); // /list_all
        System.out.println("8) Creazione di una chat di gruppo tra 3 o più utenti"); // /create_group “group_name”
        System.out.println("9) Per aggiungere un utente ad un gruppo"); // /join_G@ group_name - username1, username2…..
        System.out.println("10) Per uscire dal gruppo"); // /left_G@ group_name
    }
}
