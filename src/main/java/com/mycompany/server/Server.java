/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.server;

import java.io.IOException;
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
    // finestra in cui è possibile gestire le connessioni dei vari client
    private static ManageFrame manage;
    private static ServerSocket server;
    private static int server_port = 8080; // numero di porta da cui è possibile accedere al server
    private static List<ClientHandler> clientHandlers = new ArrayList<>();
    // private static int MAX_CONNECTIONS = 2;
    
    // metodo che si occupa della creazione del server
    public static void openServer() {
        try {
            server = new ServerSocket(server_port);
            // non è safe ma utile per il debugging veloce
            server.setReuseAddress(true);
            
            // il server rimane in ascolto
            while(true) {
                Socket client_socket = server.accept();
                System.out.println("Connessione accettata da: " + client_socket.getInetAddress());

                manage.addClient("client");
                
                // fai partire il thread relativo al client appena connesso
                ClientHandler clientHandler = new ClientHandler(client_socket);
                clientHandlers.add(clientHandler);
                
                clientHandler.sendMessage("Sei connesso al server!");
                
                // Avvia un nuovo thread per gestire la comunicazione con il client
                // con start() l'esecuzione del thread è parallela a quella del thread principale
                new Thread(clientHandler).start();

                // PrintWriter out = new PrintWriter(client_socket.getOutputStream(), true);
                // out.println("Sei connesso al server!");
            }

        } catch (IOException e) {
            System.err.println("Errore durante l'esecuzione del server: " + e.getMessage());
        }
    }
    
    // disconnette un client collegato al server
    public static void diconnectClient(Socket client_socket) {
        try {
            client_socket.close();
        } catch (IOException e) {
            System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
        }
    }
    
    // chiude il server
    public static void closeServer() {
        try {
            server.close();
        } catch(IOException e) {
            System.err.println("Errore durante la chiusura del server: " + e.getMessage());
        }
    }

    // ritorna la lista dei client collegati
    public static List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }
    
    public static void main(String[] args) {
        manage = new ManageFrame();
        manage.setTitle("Chat-Server");
        manage.setSize(900, 600);
        manage.setLocationRelativeTo(null);
        manage.setVisible(true);

        openServer();
    }
}
