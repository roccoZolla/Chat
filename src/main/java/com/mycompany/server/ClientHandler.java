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
    private String nickname;        // nome del client scelto dall'utente
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
        out.flush();
    }
    
    public Socket getSocket() {
        return this.clientSocket;
    }
    
    public String getNickname() {
        return this.nickname;
    }
    
    // estrae il nick del client
    private void extractNickname(String inputLine) {
        System.out.println("Estraggo il nickname");
        nickname = inputLine.substring("Nickname: ".length());
    }
    
    @Override
    public void run() {
        try {
            String message;
            // legge i messaggi in entrata dal server fino quando ci sono o il socket non viene chiuso
            while ((message = in.readLine()) != null) {
                // controlla che il messaggio inviato non contenga il nick del client
                if(message.startsWith("Nickname: ")) {
                    extractNickname(message);
                    Server.updateManageScreen(this);
                    Server.addNewClientToList(nickname);
                } else { // se non contiene il nick procedi all'invio dei messaggi
                    // inoltra i messaggi dal client al server
                    System.out.println("Messaggio dal client: " + message);
                    Server.sendMessageFromOneToAllClient(clientSocket, nickname + ": " + message);
                }
            }
        } catch (IOException e) {
            // Gestisci l'errore di I/O
            System.err.println("nel clientHandler Errore di I/O durante la lettura dei messaggi dal client: " + e.getMessage());
        } finally {
            // Chiudi le risorse e termina il thread in modo pulito
            closeResources();
        }
    }

    // rilascia le risorse relative al thread
    private void closeResources() {
        System.out.println("Rilascio delle risorse relative al thread del client disconnesso");
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
            
            // quando viene terminato il thread
            // aggiorna l'interfaccia
            Server.updateManageScreen(this);
            
            // rimuovi il client dalla lista
            Server.removeClientFromList(nickname);
        } catch (IOException e) {
            // Gestisci eventuali errori durante la chiusura delle risorse
            System.err.println("Errore durante la chiusura delle risorse: " + e.getMessage());
        }
    }
}
