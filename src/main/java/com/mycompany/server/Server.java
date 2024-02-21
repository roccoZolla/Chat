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
            System.out.println("Ti trovi in openServer");
            server = new ServerSocket(server_port);
            // non è safe ma utile per il debugging veloce
            server.setReuseAddress(true);
            
            // il server rimane in ascolto
            while(!server.isClosed()) {
                Socket client_socket = server.accept();
                System.out.println("Connessione accettata da: " + client_socket.getInetAddress());

                manage.addClient("Client: " + client_socket.getInetAddress() + ", " + client_socket.getPort());
                
                // fai partire il thread relativo al client appena connesso
                ClientHandler clientHandler = new ClientHandler(client_socket);
                clientHandlers.add(clientHandler);
                
                clientHandler.sendMessage("----- Sei connesso al server! -----");
                
                // Avvia un nuovo thread per gestire la comunicazione con il client
                // con start() l'esecuzione del thread è parallela a quella del thread principale
                new Thread(clientHandler).start();

                // PrintWriter out = new PrintWriter(client_socket.getOutputStream(), true);
                // out.println("Sei connesso al server!");
            }

        } catch (IOException e) {
            // System.out.println("Ti trovi in openServer");
            System.err.println("Errore durante l'esecuzione del server: " + e.getMessage());
        }
    }
    
    // si occupa di mandare un messaggio inviato da un client a tutti gli altri client connessi
    public static void sendMessageToAllClient(Socket client_socket, String msg) {
        for(ClientHandler client_handler : clientHandlers) {
            if(client_handler.getSocket() != client_socket) {
                client_handler.sendMessage(msg);
            }
        }
    }
    
    // disconnette un client collegato al server
    public static void disconnectClient(Socket client_socket) {
        try {
            if (client_socket != null) {
                client_socket.close();
            }
        } catch (IOException e) {
            System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
        }
    }
    
    // chiude il server
    public static void closeServer() {
        try {            
            if(server != null) {
                server.close();
            }
            
            // rilascia le risorse per tutti i clienti connessi
            for(ClientHandler client_handler : clientHandlers) {
                client_handler.closeResources();
            }
            
            if(manage != null) {
                manage.dispose();
            }
        } catch(IOException e) {
            System.err.println("Errore durante la chiusura del server: " + e.getMessage());
        }
        
        System.out.println("Ti trovi in closeServer");        
        System.out.println("server chiuso correttamente");
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
