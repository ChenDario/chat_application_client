package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

public class ReceiveThread extends Thread {
    
    // Colori e stili per i testi
    String RED_TEXT = "\033[31m";
    String GREEN_TEXT = "\033[32m";
    String YELLOW_TEXT = "\033[33m";
    String MAGENTA_TEXT = "\033[35m";
    String BLUE_TEXT = "\033[36m";
    String BOLD_TEXT = "\033[1m";
    String RESET_TEXT = "\033[0m";

    private BufferedReader in;
    private DataOutputStream out;
    private HashMap<String, String> group_codes;
    private volatile boolean running = true; 
    private Encryption safe_message;
    private HashMap<String, String> users_publickey;

    public ReceiveThread(BufferedReader in, DataOutputStream out, HashMap<String, String> group_codes, Encryption safe_message, HashMap<String, String> users_key) {
        this.in = in;
        this.out = out;
        this.group_codes = group_codes;
        this.safe_message = safe_message;
        this.users_publickey = users_key;
    }

    @Override
    public void run() {
        // Il Thread deve sempre stare in ascolto per possibili messaggi
        while (running) {
            try {
                String message = in.readLine(); // Leggi il messaggio

                if (message == null || message.equals("Exit")) { 
                    System.out.println("Client disconnesso.");
                    stopThread(); // blocca il thread
                    break; // Esce dal ciclo
                }
                
                // Elabora il messaggio ricevuto
                status_codes(message, this.in);

            } catch (IOException e) {
                System.err.println("Errore nella lettura dal server: " + e.getMessage());
                stopThread(); // Arresta il thread in caso di errore
            }
        }

        // Chiude input & output quando il thread si ferma
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            System.err.println("Errore nella chiusura delle risorse: " + e.getMessage());
        }
    }

    public void stopThread() {
        running = false; 
    }

    public void status_codes(String server_response, BufferedReader in) {
        try {
            String messaggio = "";

            // Mancano ancora diversi messaggi di errore e risposta
            switch (server_response) {
                //Ricezione e salvattaggio delle chiavi pubbliche degli altri utenti
                case "PUBLIC_KEY":
                    saveKey();
                    break;

                case "RCV_100": // Ricezione del messaggio (invio messaggio da parte di un altro utente)
                    messaggio = in.readLine();
                    System.out.println(decrypt_message(messaggio));
                    break;

                case "RCV_200": // Conferma creazione di un gruppo
                    // Leggi il nome del gruppo e il codice per salvarli in una HashMap
                    String group_name = in.readLine();
                    String group_code = in.readLine();

                    // HashMap contenente il nome del gruppo e il suo relativo codice
                    this.group_codes.put(group_name, group_code);
                    System.out.println("Gruppo creato. Nome: " + group_name);
                    break;

                case "RMV_200":
                    // Leggo nome e codice gruppo
                    String grp_name = in.readLine();
                    String grp_code = in.readLine();

                    this.group_codes.remove(grp_name, grp_code); // Rimozione dal hashMap del gruppo
                    System.out.println("Uscito dal gruppo: " + grp_name + " riuscita");
                    break;    

                case "CL_200": // Messaggio di conferma per il gruppo creato
                    System.out.println(GREEN_TEXT + "Group Created" + RESET_TEXT);
                    System.out.println("Enter /join_G@ group_name - username1, username2...... To add the users to the group");
                    break;

                case "SUCC_200": // Richiesta completata con successo
                    System.out.println(GREEN_TEXT + "Richiesta completata con successo" + RESET_TEXT);
                    break;

                case "SRV_200": // Risposta del server
                    messaggio = in.readLine();
                    System.out.println(messaggio);
                    break;

                case "SUCC_201": // Conferma dell'invio del messaggio
                    System.out.println("Messaggio inviato con successo");
                    break;

                case "GRP_INFO": // Vieni aggiunto in un gruppo
                    group_add(in);
                    break;

                case "ERROR_400": // Errore carattere non valido
                    System.out.println(RED_TEXT + "Invalid Character present, RETRY" + RESET_TEXT);
                    break;

                case "ERROR_403": // Utente già presente
                    System.out.println(RED_TEXT + "Utente già presente" + RESET_TEXT);
                    break;

                case "ERROR_404": // Not found
                    System.out.println("- - ERROR - - REQUEST NOT FOUND");
                    break;

                case "ERROR_404_G": // Gruppo not found
                    System.out.println("Group NOT FOUND");
                    break;

                case "ERROR_404_P": // Chat not found
                    System.out.println("User NOT FOUND");
                    break;

                case "ERROR_405":
                    System.out.println(RED_TEXT + "Request Not Found" + RESET_TEXT);
                    break;

                case "ERROR_406": 
                    System.out.println("All available user added to the group");
                    break;
                
                case "ERROR_407": 
                    System.out.println("Unico utente presente, messaggio di broadcast fallito");
                    break;

                case "ERROR_500":
                    System.out.println("Internal Server Error");
                    break;

                default: 
                    System.out.println("- - ERROR PROCESSING THE RESPONSE FROM THE SERVER - - ");
                    System.out.println("Server: " + server_response); // Debug
                    break;
            }
        } catch (Exception e) {
            System.out.println("- - ERROR - - ");
        }
    }

    synchronized public void saveKey() throws IOException {
        String user = in.readLine();
        String key = in.readLine();

        //Salvataggio della chiave pubblica dell'utente di destinazione
        users_publickey.put(user, key);
    }

    //Decrypt the message
    public String decrypt_message(String message){
        String x = message.substring(5);// Rimuove "from " per ottenere solo la parte che ci interessa
        String[] received_message = x.split(":", 2);

        String encrypted_message = received_message[1].trim(); // Rimuovi eventuali spazi all'inizio o alla fine

        //              Nome utente                 Messaggio decifrato
        return "From " + received_message[0] + ": " + safe_message.decrypt(new BigInteger(encrypted_message));
    }

    // Quando qualcuno ti aggiunge a un gruppo
    public void group_add(BufferedReader in) throws IOException {
        // Leggi il nome del gruppo e il codice per salvarli in una HashMap
        String group_name = in.readLine();
        String group_code = in.readLine();

        // Aggiungi all'HashMap il nome del gruppo e il suo relativo codice
        this.group_codes.put(group_name, group_code);

        System.out.println("You have been added to the group: " + group_name);
    }
}
