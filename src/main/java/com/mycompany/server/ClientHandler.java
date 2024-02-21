/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

// ClientHandler.java
import java.io.*;
import java.net.*;

// classe che si occcupa di gestire i client connessi al server
public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void run() {
        try {
            String inputLine;
            // legge i messaggi in entrata dal server
            while ((inputLine = in.readLine()) != null) {
                // inoltra i messaggi dal client al server
                System.out.println("Messaggio dal client: " + inputLine);
            }
        } catch (IOException e) {
            // Gestisci l'errore di I/O
            System.err.println("Errore di I/O durante la lettura dei messaggi dal client: " + e.getMessage());
        } finally {
            // Chiudi le risorse e termina il thread in modo pulito
            closeResources();
        }
    }

    // rilascia le risorse relative al thread
    private void closeResources() {
        try {
            // chiudi flusso di input
            if (in != null) {
                in.close();
            }
            
            // chiudi il flusso di output
            if (out != null) {
                out.close();
            }
            
            // Chiudi il socket del client
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            // Gestisci eventuali errori durante la chiusura delle risorse
            System.err.println("Errore durante la chiusura delle risorse: " + e.getMessage());
        }
    }
}
