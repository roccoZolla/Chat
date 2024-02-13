/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    
    public static void main(String[] args) {
        // creazione del server
        int port = 49152;
        
        try {
            ServerSocket server = new ServerSocket(port);
            // non è safe ma utile per il debugging veloce
            server.setReuseAddress(true);

            while(true) {
                System.out.println("In attesa di connessioni...");
                
                Socket client_socket = server.accept();
                System.out.println("Connessione accettata da: " + client_socket.getInetAddress());
                
                // Crea un nuovo gestore client e aggiungilo alla lista dei gestori
                ClientHandler clientHandler = new ClientHandler(client_socket);
                clientHandlers.add(clientHandler);
                
                clientHandler.sendMessage("Sei connesso al server!");
                
                // Avvia un nuovo thread per gestire la comunicazione con il client
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Errore durante l'esecuzione del server: " + e.getMessage());
        }
        
        // server.close() per chiudere il server, associare questo metodo ad un possibile bottone nell'ui
    }
    
    
    // ritorna la lista dei client collegati
    public static List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }
}
