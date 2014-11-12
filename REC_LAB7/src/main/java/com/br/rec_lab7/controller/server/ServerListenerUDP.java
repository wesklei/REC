/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.controller.server;

import com.br.rec_lab7.controller.CalculoTempo;
import com.br.rec_lab7.controller.Fibonacci;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
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
            welcomeSocket = new DatagramSocket(port,addr);
            
            byte[] incomingData = new byte[1024];

            isClossed = false;
            logger.debug("=>UDP: Iniciando thread de escuta para comunicacao com o cliente ...");
            logMensagens.setText(logMensagens.getText() + "\n" + "UDP: =>Iniciando thread de escuta para comunicacao com o cliente ...");

            List<ThreadClienteCommunicUDP> serversThreads = new ArrayList<ThreadClienteCommunicUDP>();
            while (!isClossed) {

                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                welcomeSocket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();

                if (data != null) {
                    ThreadClienteCommunicUDP threadCliente = new ThreadClienteCommunicUDP(incomingPacket, welcomeSocket);
                    threadCliente.start();

                    serversThreads.add(threadCliente);

                }
                boolean isAlive = true;
                while (isAlive) {
                    isAlive = false;
                    for (ThreadClienteCommunicUDP c : serversThreads) {
                        if (c.isAlive()) {
                            isAlive = true;
                        }
                    }
                }

                for (ThreadClienteCommunicUDP c : serversThreads) {
                    logMensagens.setText(logMensagens.getText() + "\n " + c.getLogMensagens());
                }
                logMensagens.setText(logMensagens.getText() + "\n " + "UDP:  Tempo medio decorrido: " + CalculoTempo.getMedia() + " ms");

                logger.info("UDP: Tempo medio decorrido: " + CalculoTempo.getMedia() + " ms");
                serversThreads = new ArrayList<ThreadClienteCommunicUDP>();
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
