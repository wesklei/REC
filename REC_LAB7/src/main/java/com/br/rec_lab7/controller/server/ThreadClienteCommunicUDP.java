/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.controller.server;

import com.br.rec_lab7.model.comunic.Mensagem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ThreadClienteCommunicUDP extends Thread {

    private final Logger logger = Logger.getLogger(ThreadClienteCommunicUDP.class.getName());

    private String logMensagens;
    private ObjectOutputStream outToCleinte;
    private ObjectInputStream inFromCliente;
    private final DatagramPacket pacoteRecebido;
    private final DatagramSocket datagramSocket;
    private byte[] data;
    private final InetAddress ip;
    private final Integer portRecebido;
    private final ServerCommand comandosCliente;

    public ThreadClienteCommunicUDP(DatagramPacket pacoteRecebido, DatagramSocket datagramSocket) throws IOException {
        this.pacoteRecebido = pacoteRecebido;
        this.datagramSocket = datagramSocket;
        this.comandosCliente = new ServerCommand();
        this.ip = pacoteRecebido.getAddress();
        this.portRecebido = pacoteRecebido.getPort();
        logMensagens = "\n";
        this.data = pacoteRecebido.getData(); //leu primeiro na outra thread
    }

    @Override
    public void run() {
        Object comandoDoCliente;
        Object retornoParaCliente;
        try {

            comandoDoCliente = getObjectLido();

            if (comandoDoCliente != null) {

                logger.debug("Cliente Conectou! Thread de comunicacao com cliente esperando dados ...");
                logarMensagem("Cliente Conectou! Thread de comunicacao com cliente esperando dados ...");
                if (comandoDoCliente instanceof Mensagem) {
                    logger.debug("Comandos do Cliente: " + comandoDoCliente.toString());
                    logarMensagem("Comandos do Cliente: " + comandoDoCliente.toString());

                    retornoParaCliente = (Object) comandosCliente.parseMensagem(comandoDoCliente);

                    logger.debug("Comandos para o Cliente: " + retornoParaCliente.toString());
                    logarMensagem("Comandos para o Cliente: " + retornoParaCliente.toString());
                    if (!enviaParaCliente(retornoParaCliente)) {
                        logger.debug("Erro enquanto envia para o cliente");
                        logarMensagem("Erro enquanto envia para o cliente");

                    }

                } else if (inFromCliente.read() == -1) {//conexao fechou
                    logger.debug("Cliente fechou a conexao");
                    logarMensagem("Cliente fechou a conexao");

                } else {
                    logger.debug("Recebeu comando do Cliente null");
                    logarMensagem("Recebeu comando do Cliente null");

                }
            }

            fecharConexao(); //encerra tudo
        } catch (EOFException ex) {
            logger.log(Level.INFO, "Cliente fechou a conexao");
            logarMensagem("Cliente fechou a conexao");
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Erro na conexao com o cliente", ex);
            logarMensagem("Erro na conexao com o cliente");
        } catch (ClassNotFoundException ex) {
            logger.log(Level.ERROR, "Erro na conversao do objeto mensagem recebido do ciente", ex);
            logarMensagem("Erro na conversao do objeto mensagem recebido do ciente");
        }
    }

    private boolean enviaParaCliente(Object o) throws IOException {
        //if (outToCleinte == null && clienteSocket != null) { //precisa checar sempre

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outToCleinte = new ObjectOutputStream(outputStream);
        outToCleinte.writeObject(o);
        outToCleinte.flush();

        pacoteRecebido.setData(outputStream.toByteArray());
        datagramSocket.send(pacoteRecebido);
        return true; //se conseguiu
    }

    private void recebeDoCliente() throws IOException, ClassNotFoundException {
        byte[] incomingData = new byte[1024];
        DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
        datagramSocket.receive(incomingPacket);
        data = incomingPacket.getData();
    }

    private Object getObjectLido() throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        inFromCliente = new ObjectInputStream(in);

        if (inFromCliente != null) {
            return inFromCliente.readObject();
        }

        return null;
    }

    private void fecharConexao() throws IOException {
        if (inFromCliente != null) {
            inFromCliente.close();
        }
        if (outToCleinte != null) {
            outToCleinte.flush();
            outToCleinte.close();
        }
    }

    private void logarMensagem(String msg) {
        logMensagens += "UDP: " + msg + "\n";
    }

    public String getLogMensagens() {
        return logMensagens;
    }
}
