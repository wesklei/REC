/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.controller.server;

import com.br.rec_lab7.controller.CalculoTempo;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ServerListenerTCP extends Thread {

    private static final Logger logger = Logger.getLogger(ServerListenerTCP.class.getName());

    private ServerSocket welcomeSocket;
    private boolean isClossed;
    private final Integer port;
    private final String ip;
    private final JTextArea logMensagens;

    /**
     * the port to list and how much values will start
     *
     * @param port
     * @param ip
     * @param logMensagens
     */
    public ServerListenerTCP(Integer port, String ip, JTextArea logMensagens) {
        this.port = port;
        this.ip = ip;
        this.logMensagens = logMensagens;
    }

    @Override
    public void run() {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            welcomeSocket = new ServerSocket(port,5000,addr);
            
            isClossed = false;
            logger.debug("TCP: =>Iniciando thread de escuta para comunicacao com o cliente ...");
            logMensagens.setText(logMensagens.getText() + "\n" + "=>Iniciando thread de escuta para comunicacao com o cliente ...");

            while (!isClossed) {

                Socket connectionSocket = welcomeSocket.accept();

                if (connectionSocket != null) {
                    ThreadClienteCommunicTCP threadCliente = new ThreadClienteCommunicTCP(connectionSocket, logMensagens);
                    threadCliente.start();
                }
             }

        } catch (IOException ex) {
            if (!isClossed) { //se fechou ignora os erros
                logger.log(Level.ERROR, null, ex);
            }
        }

    }

    public void fecharConexao() throws IOException, Throwable {

        if (welcomeSocket != null) {
            welcomeSocket.close();
            isClossed = true;
            logger.debug("TCP: =>Finalizou a escuta no servidor");
            logMensagens.setText(logMensagens.getText() + "\n" + "TCP: =>Finalizou a escuta no servidor");
        }
    }

    public boolean isIsClossed() {
        return isClossed;
    }

    public void setIsClossed(boolean isClossed) {
        this.isClossed = isClossed;
    }

}
