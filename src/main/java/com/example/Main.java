package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    private static String username;
    private static Encryption safe_message = new Encryption();

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        HashMap<String, String> group_codes = new HashMap<>();
        HashMap<String, String> users_key = new HashMap<>();

        
        //colori e stili per i testi
        String RED_TEXT = "\033[31m";
        String GREEN_TEXT = "\033[32m";
        String YELLOW_TEXT = "\033[33m";
        String MAGENTA_TEXT = "\033[35m";
        String BLUE_TEXT = "\033[36m";
        String BOLD_Text = "\033[1m";

        String RESET_TEXT = "\033[0m";


        Socket socket = new Socket("localhost", 3000);
        System.out.println(YELLOW_TEXT + "Il client si è collegato" + RESET_TEXT);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // input user
        Scanner scanner = new Scanner(System.in);

        // Validazione della connessione tramite l'inserimento dell'usernamem
        Input_username name = new Input_username(scanner, in, out);
        username = name.input_username();
        System.out.println("Username: " + username + "\n");

        //Generazione delle chiavi pubbliche e private
        safe_message.generateKeys(1024);
        String publicKey = safe_message.getPublicKey();
        //Invio della chiave privata al server per renderlo nota a tutti
        out.writeBytes("PublicKey " + publicKey + "\n");

        // Avvia la ricezione dei messaggi
        ReceiveThread r = new ReceiveThread(in, out, group_codes, safe_message, users_key);
        r.start();

        sendMessage(in, out, scanner, users_key);

        // Se per qualche motivo esce dal loop, significa che sta lasciando la chat
        in.close();
        out.close();
        scanner.close();
        socket.close();

    }

    public static void sendMessage(BufferedReader in, DataOutputStream out, Scanner scan, HashMap<String, String> users_key) throws IOException, InterruptedException {
        Thread.sleep(500);

        String message = "";

        //colori e stili per i testi
        String RED_TEXT = "\033[31m";
        String GREEN_TEXT = "\033[32m";
        String YELLOW_TEXT = "\033[33m";
        String MAGENTA_TEXT = "\033[35m";
        String BLUE_TEXT = "\033[36m";
        String BOLD_Text = "\033[1m";

        String RESET_TEXT = "\033[0m";

        // Loop per inviare messaggi
        while (true) {
            stampaMenu();
            boolean x; // in caso si debba ripetere il ciclo

            do {
                x = false;
                String scelta = "";
                //Inserimento dell'operazione
                System.out.println("Scrivi il numero corrispondente all' azione che vorresti fare");
                scelta = scan.nextLine();

                switch (scelta) {
                    case "0": // Disconnessione...
                        message = "exit";
                        break;

                    case "1": //Inviare un messaggio ad un altro utente" @username “message”
                        System.out.println("Scrivi il nome utente a cui inviare il messaggio");
                        String nomeUtenteEsistente = scan.nextLine();
                        //Da inserire se l'utente inserito esiste o meno(Controllo da eseguire subito)
                        //Controllo se ho la public key dell'utente di destinazione, in caso negativo la richiedo subito
                        findPublicKey(nomeUtenteEsistente, out, users_key);
                        message = "@" + nomeUtenteEsistente + " ";
                        //Inserimento messaggio
                        System.out.println("Scrivi il messaggio: ");
                        String text1 = scan.nextLine();
                        //Codifica del messaggio
                        message += encrypt_message(text1, nomeUtenteEsistente, users_key);
                        break;

                    case "2": //Inviare un messaggio ad un gruppo G@group_name “message” 
                        System.out.println("Scrivi il nome del gruppo a cui inviare il messaggio");
                        String GruppoEsistente = scan.nextLine();
                        message = "G@" + GruppoEsistente + " ";
                        System.out.println("Scrivi il messaggio: ");
                        String text2 = scan.nextLine();
                        message = message + text2;
                        //System.out.println(message);
                        break;

                    case "3": //Inviare un messaggio a tutti gli utenti registrati // @All “message”
                        System.out.println("Scrivi il messaggio da inviare a tutti: ");
                        String messaggioTutti = scan.nextLine();
                        message = "@All " + messaggioTutti;
                        break;

                    case "4": //Lista degli utenti attivi @_list
                        message = "@_list";
                        break;

                    case "5": //lista dei gruppi attivi G@_list
                        message = "G@_list";
                        break;

                    case "6": //Lista dei membri di un gruppo /users_group nome_gruppo
                        System.out.println("Digita il nome del gruppo per vedere i suoi partecipanti");
                        String gruppoEsistente = scan.nextLine();
                        message = "/users_group " + gruppoEsistente;
                        break;

                    case "7": //Lista di tutte le chat attive (sia gruppi e sia private) /list_all
                        message = "/list_all";
                        break;

                    case "8": //Creazione di una chat di gruppo tra 3 o più utenti /create_group “group_name”
                        System.out.println("Digitare il nome del gruppo da creare");
                        String nomeGruppoDaCreare = scan.nextLine();
                        message = "/create_group " + nomeGruppoDaCreare;
                        break;

                    case "9": //Per aggiungere un utente ad un gruppo /join_G@ group_name - username1, username2…..
                        System.out.println("Inserisci il nome del gruppo da qui vuoi aggiungere un altro utente");
                        String nomeGruppoEsistente = scan.nextLine();
                        message = "/join_G@ " + nomeGruppoEsistente + " - ";
                        String rispostaSi;
                        do {
                            System.out.println("Inserisci il nome utente da inserire");
                            String nomeUtente = scan.nextLine();
                            message += nomeUtente + ", "; //aggancia ogni volta utente nuovo
                            System.out.println("Vuoi inserire un altro utente? digitare 'si' se la risposta è affermativa");
                            rispostaSi = scan.nextLine();
                        } while (rispostaSi.equalsIgnoreCase("si"));
                        if (message.length() > 0 && message.charAt(message.length() - 2) == ',') { 
                            //togliere l'ultimo carrattere perchè è una ,
                            message = message.substring(0, message.length() - 2);
                        } //dovrebbe essere a posto
                        //System.out.println(message);
                        break;

                    case "10": //10) Per uscire dal gruppo /left_G@ group_name
                        System.out.println("Inserisci il nome del gruppo da qui vuoi uscire");
                        String nomeEliminabile = scan.nextLine();
                        message = "/left_G@ " + nomeEliminabile;
                        break;

                    default:
                        System.out.println(RED_TEXT + "Request Not Found" + RESET_TEXT);
                        x = true;
                        break;
                }

            } while (x);// in caso inserisca una stringa non valida

            // Condizione di uscita
            if (message.equalsIgnoreCase("exit")) {
                System.out.println(YELLOW_TEXT + "Disconnessione..." + RESET_TEXT);
                break;
            }

            // Invia il messaggio al server
            out.writeBytes(message + "\n");

            // Questo sleep permette di far eseguire la richiesta e farla stampare prima che
            // ricominci il loop (cosa visiva)
            Thread.sleep(1000);
        }
    }

    public static String encrypt_message(String message, String user, HashMap<String, String> users_key){
        String public_key = users_key.get(user);
        String[] key_dest = public_key.split(" : ");

        return safe_message.encrypt(message, new BigInteger(key_dest[0]), new BigInteger(key_dest[1]));
    }

    public static void findPublicKey(String user, DataOutputStream out, HashMap<String, String> users_key) throws IOException{
        if(users_key.get(user) == null)
            getPublicKey(user, out);
        //Se non ha la chiave la richiede al server
    }

    public static void getPublicKey(String user_dest, DataOutputStream out) throws IOException{
        out.writeBytes("/request_key " + user_dest);
    }

    public static void stampaMenu() { // funzione per stampare il menu
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
        // System.out.println("11) lista degli utenti attivi"); // /show_command //non
        // servirebbe quindi bho
    }
}