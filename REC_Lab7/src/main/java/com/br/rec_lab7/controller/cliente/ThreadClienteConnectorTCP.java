/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.controller.cliente;

import com.br.rec_lab7.controller.CalculoTempo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import org.apache.log4j.Logger;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ThreadClienteConnectorTCP extends Thread {

    private String logMensagens;
    private final String ip;
    private final Integer port;
    private final int limiteFibonacci;
    private final int timeout;
    private final String identificador; //identifica o clienet
    private final ClienteCommand clienteCommand;
    private final int codigoThread; //identifica a thread
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private Socket communicSocket;

    private final Logger logger = Logger.getLogger(ThreadClienteConnectorTCP.class.getName());

    public ThreadClienteConnectorTCP(String ip, Integer port, int limiteFibonacci, String identificador, int codigoThread, int timeout) {
        this.ip = ip;
        this.port = port;
        this.clienteCommand = new ClienteCommand();
        this.limiteFibonacci = limiteFibonacci;
        this.timeout = timeout;
        this.identificador = identificador;
        this.codigoThread = codigoThread;

        logMensagens = "Log para Cliente '" + identificador + "' na thread '" + codigoThread + "'\n";
    }

    @Override
    public void run() {
        logger.debug("=>Iniciando comunicacao com o servidor ");
        logarMensagem("=>Iniciando comunicacao com o servidor ");
        long startTime = System.currentTimeMillis();
        try {
            String msgRetorno;

            //estabelece a conexao
            communicSocket = new Socket();
            SocketAddress sockaddr = new InetSocketAddress(ip, port);
            communicSocket.connect(sockaddr, timeout);//define 10s de timeout


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

            logger.info("Terminando conexao com o servidor!");
            logarMensagem("Terminando conexao com o servidor!");
            fecharConexao(); //terminou

        } catch (IOException ex) {
            logger.error("Erro na conexao do cliente com o servidor", ex);
            logarMensagem("Erro na conexao do cliente com o servidor");
        } catch (ClassNotFoundException ex) {
            logarMensagem("Erro na conexao do cliente com o servidor durante a leitura da classe do objeto");
            logger.error("Erro na conexao do cliente com o servidor durante a leitura da classe do objeto", ex);
        }
        long endtime = System.currentTimeMillis();

        long totalTime = endtime - startTime;
        CalculoTempo.addNovoTempo(totalTime);
        logger.info("Tempo decorrido no cliente " + identificador + " na thread " + codigoThread + " eh: " + totalTime + " ms");
        logarMensagem("Tempo decorrido no cliente " + identificador + " na thread " + codigoThread + " eh: " + totalTime + " ms");
    }

    private boolean enviaParaServidor(Object o) throws IOException {
        if (outToServer == null && communicSocket != null) { //precisa checar sempre
            outToServer = new ObjectOutputStream(communicSocket.getOutputStream());
        }

        if (outToServer != null) {
            outToServer.writeObject(o);
            outToServer.flush();

            return true; //se conseguiu
        }

        return false; //se fechou a conexao ou nao conseguiu
    }

    private Object recebeDoServidor() throws IOException, ClassNotFoundException {
        if (inFromServer == null && communicSocket != null) { //precisa checar sempre
            inFromServer = new ObjectInputStream(communicSocket.getInputStream());

        }

        if (inFromServer != null) {
            return inFromServer.readObject();
        }

        return null;
    }

    private void fecharConexao() throws IOException {
        if (inFromServer != null) {
            inFromServer.close();
        }
        if (outToServer != null) {
            outToServer.flush();
            outToServer.close();
        }
        if (communicSocket != null && communicSocket.isConnected()) {
            communicSocket.close();
        }
    }

    private void logarMensagem(String msg) {
        logMensagens += "TCP: " + msg + "\n";
    }

    public String getLogMensagens() {
        return logMensagens;
    }
}
