/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rocco
 */
// 49152 porta dinamica non registrata
// 127.0.0.1 local host
public class Server {

    private static List<ClientHandler> clientHandlers = new ArrayList<>();
    // private static int MAX_CONNECTIONS = 2;

    public static void main(String[] args) {
        // creazione del server
        int port = 49152;
        int connections = 0;

        try {
            ServerSocket server = new ServerSocket(port);
            // non è safe ma utile per il debugging veloce
            server.setReuseAddress(true);

            Socket client_socket = server.accept();
            System.out.println("Connessione accettata da: " + client_socket.getInetAddress());
            
            PrintWriter out = new PrintWriter(client_socket.getOutputStream(), true);
            
            out.println("Sei connesso al server!");
            
            out.println("Chiusura connessione...");
            
            // termina la connessione
            client_socket.close();
            server.close();

            // quando il numero di connessioni è raggiunto 
            // spostati avvia la chat tra i due client
        } catch (IOException e) {
            System.err.println("Errore durante l'esecuzione del server: " + e.getMessage());
        }
    }

    // ritorna la lista dei client collegati
    public static List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }
}
