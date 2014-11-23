/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.controller.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JTextArea;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ServerListenerUDP extends Thread {

    private static final Logger logger = Logger.getLogger(ServerListenerUDP.class.getName());

    private DatagramSocket welcomeSocket;
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
    public ServerListenerUDP(Integer port, String ip, JTextArea logMensagens) {
        this.port = port;
        this.ip = ip;
        this.logMensagens = logMensagens;
    }

    @Override
    public void run() {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            welcomeSocket = new DatagramSocket(port, addr);

            byte[] incomingData = new byte[1024];

            isClossed = false;
            logger.debug("=>UDP: Iniciando thread de escuta para comunicacao com o cliente ...");
            logMensagens.setText(logMensagens.getText() + "\n" + "UDP: =>Iniciando thread de escuta para comunicacao com o cliente ...");

            while (!isClossed) {

                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                welcomeSocket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();

                if (data != null) {
                    //new socket created with random port for thread
                    //precisa trocar a porta para a thread poder "manter uma conexao"
                    //com o cliente, se nao todos os clientes irao usar o mesmo socket
                    //e dara problema
                    DatagramSocket threadSocket = new DatagramSocket();
                    
                    ThreadClienteCommunicUDP threadCliente = new ThreadClienteCommunicUDP(incomingPacket, threadSocket, logMensagens);
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
            logger.debug("UDP:  =>Finalizou a escuta no servidor");
            logMensagens.setText(logMensagens.getText() + "\n" + "UDP:  =>Finalizou a escuta no servidor");
        }
    }

    public boolean isIsClossed() {
        return isClossed;
    }

    public void setIsClossed(boolean isClossed) {
        this.isClossed = isClossed;
    }

}
