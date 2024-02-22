/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.server;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
// import java.awt.GridLayout;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author rocco
 */
public class ManageFrame extends javax.swing.JFrame {

    /**
     * Creates new form ManageFrame
     */
    public ManageFrame() {
        initComponents();
    }
    
    public void addClient(String client_name, Socket client_socket) {
        System.out.println("chiamata add Client");
        
        // aggiungi etichetta relativa al client
        JLabel label = new JLabel(client_name);
        label.setForeground(Color.BLACK);
        manage_panel.add(label);
        
        // aggiungi bottone per disconnettere il client
        JButton disconnect_button = new JButton();
        disconnect_button.setText("Disconnetti");
        disconnect_button.putClientProperty("clientSocket", client_socket); // associa client al bottone
        disconnect_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnect_client(client_socket);
            }
        });
        manage_panel.add(disconnect_button);
        
        manage_panel.revalidate();
        manage_panel.repaint();
    }
    
    // rimuovi bottoni ed etichetta relativa al client
    public void removeClient(Socket client_socket) {
        System.out.println("Rimozione componenti relativi al client");

        // Cerca il pulsante e l'etichetta relativi al clientSocket e rimuovili
        for (Component component : manage_panel.getComponents()) {
            System.out.println(component.getClass().getName());
            if (component instanceof JButton) {
                System.out.println("pulsante da rimuovere trovato");
                JButton button = (JButton) component;
                Socket socket = (Socket) button.getClientProperty("clientSocket");
                if (socket != null && socket.equals(client_socket)) {
                    manage_panel.remove(button);
                    // break;
                }
            } else if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (label.getText().contains("Client: " + client_socket.getInetAddress() + ", " + client_socket.getPort())) {
                    System.out.println("rimozione label");
                    manage_panel.remove(label);
                    // break;
                }
            }
        }

        manage_panel.revalidate();
        manage_panel.repaint();
    }

    private void disconnect_client(Socket client_socket) {
        // System.out.println("disconnessione del client..."); // aggiungi info relative al client
        Server.disconnectClient(client_socket);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        connected_client_label = new javax.swing.JLabel();
        shutdown_server_button = new javax.swing.JButton();
        manage_panel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        connected_client_label.setText("Client connessi");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        getContentPane().add(connected_client_label, gridBagConstraints);

        shutdown_server_button.setText("Spegni server");
        shutdown_server_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shutdown_server_buttonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        getContentPane().add(shutdown_server_button, gridBagConstraints);

        manage_panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        manage_panel.setLayout(new java.awt.GridLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 20, 0);
        getContentPane().add(manage_panel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void shutdown_server_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shutdown_server_buttonActionPerformed
        // TODO add your handling code here:
        Server.closeServer();
    }//GEN-LAST:event_shutdown_server_buttonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel connected_client_label;
    private javax.swing.JPanel manage_panel;
    private javax.swing.JButton shutdown_server_button;
    // End of variables declaration//GEN-END:variables
}
