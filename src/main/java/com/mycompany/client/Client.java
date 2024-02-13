/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author rocco
 */
public class Client {
    static String serverAddress = "localhost"; // Indirizzo IP del server
    static int serverPort = 49152; // Porta del server

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
            Socket socket = new Socket(serverInetAddress, serverPort);

            // Flussi di input e output per comunicare con il server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            System.out.println("In attesa di connessione al server");
            
            // Legge messaggi inviati dal server
            String response;
            while ((response = input.readLine()) != null) {
                System.out.println("Messaggio dal server: " + response);
            }
            
            boolean exit_flag = false;  // esci dal ciclo di invio mesasggi al server

            // inserisci testo e invialo al server
            do {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Inserisci una stringa:");
                String text_input = scanner.nextLine();

                output.println(text_input);
                
                if(text_input.equals("esc")) exit_flag = true;
            } while(!exit_flag);


            // Chiude il socket quando la comunicazione è terminata
            System.out.println("Comunicazione terminata. Chiusura del client.");
            socket.close();
        } catch (IOException e) {
            System.err.println("Errore durante la connessione al server: " + e.getMessage());
        }
        
    }
    
}
