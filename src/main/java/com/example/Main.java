package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    private static String username;

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        HashMap<String, String> group_codes = new HashMap<>();

        Socket socket = new Socket("localhost", 3000);
        System.out.println("Il client si è collegato");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // input user
        Scanner scanner = new Scanner(System.in);

        // Validazione della connessione tramite l'inserimento dell'usernamem
        Input_username name = new Input_username(scanner, in, out);
        username = name.input_username();
        System.out.println("Username: " + username + "\n");

        // Avvia la ricezione dei messaggi
        ReceiveThread r = new ReceiveThread(in, out, group_codes);
        r.start();

        sendMessage(in, out, scanner);

        // Se per qualche motivo esce dal loop, significa che sta lasciando la chat
        in.close();
        out.close();
        scanner.close();
        socket.close();

    }

    public static void sendMessage(BufferedReader in, DataOutputStream out, Scanner scan)
            throws IOException, InterruptedException {

        String message = "";

        System.out.println("Enter /show_command to print all the available commands: ");

        // Loop per inviare messaggi
        while (true) {
            /*
             * String[] commands = {
             * //Sending a message
             * "- - TO SEND A MESSAGE (no need for the \"\" in the actual command) - - ",
             * "@nome_username \"message\" to send a message to user nome_username",
             * "@All \"message\" to send a message to everyone",
             * "G@group_name \"message\" to send a message to group group_name",
             * //Get Lists
             * "- - LISTS - - ",
             * "@_list show all available private chats",
             * "G@_list show all available groupchats",
             * "/list_all to show both the available private chats and groupchats",
             * "/show_command Print all the executable commands",
             * //Create group or add user
             * "- - GROUP CREATION / USER FRIENDSHIP AND OTHERS- - ",
             * "/create_group \"group_name\" to create a group with group_name",
             * "/add_user \"username\" to add user with username",
             * "/accept Accept the last friendship request",
             * "/accept_all Accept all friendship requests",
             * "/reject Reject the last friendship request",
             * "/reject_all Reject all friendship requests",
             * "/join_G@ To add a user to the groupchat",
             * "Enter EXIT to exit"
             */

            boolean x = false; // in caso si debba ripetere il ciclo
            do {
                x = false;

                stampaMenu();

                String scelta = scan.nextLine();

                /*to do list:
                 * riempire i case
                 * verificare che non ci siano conflitti nelle richieste
                 * opsionale: guardare che risposte mi da
                 * provare a dare la possibilità di far rimanere lo stesso utente destinatario 
                 */

                switch (scelta) {
                    case "0": // Disconnessione...
                        message = "exit";
                        break;

                    case "1":

                        break;

                    case "2":

                        break;

                    case "3":

                        break;

                    case "4":

                        break;

                    case "5":

                        break;

                    case "6":

                        break;

                    case "7":

                        break;

                    case "8":

                        break;

                    case "9":

                        break;

                    case "10":

                        break;

                    default:
                        System.out.println("inserimento non valido, riprovare");
                        x = true;
                        break;
                }

            } while (x);// in caso inserisca una stringa non valida

            // System.out.println("Enter the command to execute or the message you wish to
            // send: ");
            // message = scan.nextLine(); // LEGGE IL MESSAGGIO DA TEERMINALE //DA TOGLERE
            // //VECCHIO/show_command

            // da qui in poi uguale
            // Invia il messaggio al server
            out.writeBytes(message + "\n");

            // Condizione di uscita
            if (message.equalsIgnoreCase("exit")) {
                System.out.println("Disconnessione...");
                break;
            }
            // Questo sleep permette di far eseguire la richiesta e farla stampare prima che
            // ricominci il loop (cosa visiva)
            Thread.sleep(1000);
        }
    }

    public static void stampaMenu() { // funzione per stampare il menu
        // tutti print out con le opzioni
        System.out.println("Scrivi il numero corrispondente all' azione che vorresti fare");
        System.out.println("0) uscire dall' aplicazione"); // exit
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

    // Simple Chat(Client - Server) Communication, hours spent coding: 18 (dario)
    // Ore sprecate per fare il debug degli errori per motivi stupidi: 10/18
    // Ore sprecate solo per capire il ragionamento in testa: 6
}