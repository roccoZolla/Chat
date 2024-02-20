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

public class Client {
    static String serverAddress = "localhost"; // Indirizzo IP del server
    static int serverPort = 49152; // Porta del server

    public static void main(String[] args) {
        try {
            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
            Socket socket = new Socket(serverInetAddress, serverPort);

            // Flussi di input e output per comunicare con il server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            // Thread per la ricezione dei messaggi dal server 
            // se ne occuopa il metodo sendMessage della classe ClientHandler
            Thread receiveThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println("Messaggio dal server: " + response);
                    }
                } catch (IOException e) {
                    System.err.println("Errore durante la lettura dei messaggi dal server: " + e.getMessage());
                }
            });
            receiveThread.start();

            // Thread per l'invio dei messaggi al server
            Thread sendThread = new Thread(() -> {
                try {
                    Scanner scanner = new Scanner(System.in);
                    while (true) {
                        System.out.println("Inserisci una stringa:");
                        String textInput = scanner.nextLine();
                        out.println(textInput);
                        if (textInput.equals("esc")) break;
                    }
                } catch (Exception e) {
                    System.err.println("Errore durante l'invio del messaggio al server: " + e.getMessage());
                }
            });
            sendThread.start();

            // Attendi che entrambi i thread terminino
            receiveThread.join();
            sendThread.join();

            // Chiude il socket quando la comunicazione è terminata
            System.out.println("Comunicazione terminata. Chiusura del client.");
            socket.close();
        } catch (IOException | InterruptedException e) {
            System.err.println("Errore durante la connessione al server: " + e.getMessage());
        }
    }
}
