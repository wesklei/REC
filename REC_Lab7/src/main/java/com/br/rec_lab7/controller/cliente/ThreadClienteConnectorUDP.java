/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.controller.cliente;

import com.br.rec_lab7.controller.CalculoTempo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ThreadClienteConnectorUDP extends Thread {

    private String logMensagens;
    private String ip;
    private Integer port;
    private int limiteFibonacci;
    private final boolean isTimeoutEnabled;
    private final int qtdMsg;
    private int timeout;
    private String identificador; //identifica o clienet
    private ClienteCommand clienteCommand;
    private int codigoThread; //identifica a thread
    private InetAddress inetAdress; //
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private DatagramSocket communicSocket;

    private final Logger logger = Logger.getLogger(ThreadClienteConnectorUDP.class.getName());

    public ThreadClienteConnectorUDP(String ip, Integer port, int limiteFibonacci, String identificador, int codigoThread, int timeout, int qtdMsg, boolean isTimeoutEnabled) {
        this.ip = ip;
        this.port = port;
        this.clienteCommand = new ClienteCommand();
        this.limiteFibonacci = limiteFibonacci;
        this.timeout = timeout;
        this.identificador = identificador;
        this.codigoThread = codigoThread;
        this.qtdMsg = qtdMsg;
        this.isTimeoutEnabled = isTimeoutEnabled;
        logMensagens = "Log para Cliente '" + identificador + "' na thread '" + codigoThread + "'\n";

        try {
            this.inetAdress = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            logarMensagem("Erro na conexao do cliente com o servidor durante a resolucao do ip");
            logger.error("Erro na conexao do cliente com o servidor durante a resolucao do ip", ex);
        }
    }

    @Override
    public void run() {
        logger.debug("=>Iniciando comunicacao com o servidor ");
        logarMensagem("=>Iniciando comunicacao com o servidor ");
        long startTime = System.currentTimeMillis();
        String msgRetorno;

        try {

            //estabelece a conexao
            communicSocket = new DatagramSocket();
            if (isTimeoutEnabled) {
                communicSocket.setSoTimeout(timeout);
            }

            for (int i = 0; i < qtdMsg; i++) {

                Object objToServer = clienteCommand.buildMessage(limiteFibonacci, codigoThread, identificador);
                enviaParaServidor(objToServer);

                Object retorno = recebeDoServidor();

                if (retorno != null) {
                    msgRetorno = clienteCommand.parseMensagem(retorno);
                    logger.info(retorno.toString());
                    logarMensagem(retorno.toString());
                } else {
                    logger.error("Servidor retornou null!");
                    logarMensagem("Servidor retornou null!");
                }
            }

        } catch (IOException ex) {
            logger.error("Erro na conexao do cliente com o servidor", ex);
            logarMensagem("Erro na conexao do cliente com o servidor");
        } catch (ClassNotFoundException ex) {
            logarMensagem("Erro na conexao do cliente com o servidor durante a leitura da classe do objeto");
            logger.error("Erro na conexao do cliente com o servidor durante a leitura da classe do objeto", ex);
        }

        logger.info("Terminando conexao com o servidor!");
        logarMensagem("Terminando conexao com o servidor!");
        try {
            fecharConexao(); //terminou
        } catch (IOException ex) {
            logger.error("Erro na conexao do cliente com o servidor", ex);
            logarMensagem("Erro na conexao do cliente com o servidor");
        }
        
        long endtime = System.currentTimeMillis();

        long totalTime = endtime - startTime;

        CalculoTempo.addNovoTempo(totalTime);
        logger.info("Tempo decorrido no cliente " + identificador + " na thread " + codigoThread + " eh: " + totalTime + " ms");
        logarMensagem("Tempo decorrido no cliente " + identificador + " na thread " + codigoThread + " eh: " + totalTime + " ms");
    }

    private boolean enviaParaServidor(Object o) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outToServer = new ObjectOutputStream(outputStream);
        outToServer.writeObject(o);

        byte[] data = outputStream.toByteArray();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, inetAdress, port);

        if (communicSocket != null) {
            communicSocket.send(sendPacket);

            return true; //se deu certo
        }

        return false;//se fechou a conexao ou nao deu certo

    }

    private Object recebeDoServidor() throws IOException, ClassNotFoundException {
        byte[] incomingData = new byte[1024];

        DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);

        communicSocket.receive(incomingPacket); 
        port = incomingPacket.getPort(); // precisa fazer isso, pois como eh multithread
        //o servidor ira retornar em outra porta, e o cliente se precisar mandar msg
        //para o mesmo socket do server, percisa pegar a porta q o server usou
        ByteArrayInputStream baos = new ByteArrayInputStream(incomingData);
        ObjectInputStream oos = new ObjectInputStream(baos);
        return oos.readObject();
    }

    private void fecharConexao() throws IOException {
        if (inFromServer != null) {
            inFromServer.close();
        }
        if (outToServer != null) {
            outToServer.flush();
            outToServer.close();
        }
        if (communicSocket != null) {
            communicSocket.close();
        }
        
        interrupt();
    }

    private void logarMensagem(String msg) {
        logMensagens += "UDP: " + msg + "\n";
    }

    public String getLogMensagens() {
        return logMensagens;
    }
}
