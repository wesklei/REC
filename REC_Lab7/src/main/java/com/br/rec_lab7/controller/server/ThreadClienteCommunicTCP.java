/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.controller.server;

import com.br.rec_lab7.model.comunic.Mensagem;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JTextArea;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ThreadClienteCommunicTCP extends Thread {

    private final Logger logger = Logger.getLogger(ThreadClienteCommunicTCP.class.getName());

    private ObjectOutputStream outToCleinte;
    private ObjectInputStream inFromCliente;
    private boolean isAlive;
    private final Socket clienteSocket;
    private final ServerCommand comandosCliente;
    private final JTextArea logMensagens;

    public ThreadClienteCommunicTCP(Socket clienteSocket, JTextArea logMensagens) throws IOException {
        this.clienteSocket = clienteSocket;
        this.comandosCliente = new ServerCommand();
        this.logMensagens = logMensagens;

        logger.debug("Cliente Conectou! Thread de comunicacao com cliente esperando dados ...");
        logarMensagem("Cliente Conectou! Thread de comunicacao com cliente esperando dados ...");
    }

    @Override
    public void run() {
        Object comandoDoCliente;
        Object retornoParaCliente;

        isAlive = true;
        while (isAlive) {
            try {
                comandoDoCliente = recebeDoCliente();//le do cliente a solicitacao

                if (comandoDoCliente != null) {

                    if (comandoDoCliente instanceof Mensagem) {
                        logger.debug("Comandos do Cliente: " + comandoDoCliente.toString());
                        logarMensagem("Comandos do Cliente: " + comandoDoCliente.toString());

                        retornoParaCliente = (Object) comandosCliente.parseMensagem(comandoDoCliente);

                        logger.debug("Comandos para o Cliente: " + retornoParaCliente.toString());
                        logarMensagem("Comandos para o Cliente: " + retornoParaCliente.toString());
                        if (!enviaParaCliente(retornoParaCliente)) {
                            //ocorreu algum problema
                            isAlive = false;
                            break;
                        }

                    } else if (inFromCliente.read() == -1) {//conexao fechou
                        logger.debug("Cliente fechou a conexao");
                        logarMensagem("Cliente fechou a conexao");
                        isAlive = false;
                        break;
                    } else {
                        logger.debug("Recebeu comando do Cliente null");
                        logarMensagem("Recebeu comando do Cliente null");
                        isAlive = false;
                        break;
                    }
                } else if (inFromCliente.read() == -1) {
                    logger.debug("Cliente fechou a conexao");
                    logarMensagem("Cliente fechou a conexao");
                    isAlive = false;
                }
            } catch (EOFException ex) {
                logger.debug("Cliente fechou a conexao");
                logarMensagem("Cliente fechou a conexao");
                isAlive = false;
            } catch (IOException ex) {
                logger.log(Level.ERROR, "Erro na conexao com o cliente", ex);
                logarMensagem("Erro na conexao com o cliente");
                isAlive = false;
            } catch (ClassNotFoundException ex) {
                logger.log(Level.ERROR, "Erro na conversao do objeto mensagem recebido do ciente", ex);
                logarMensagem("Erro na conversao do objeto mensagem recebido do ciente");
                isAlive = false;
            }
        }

        try {
            fecharConexao(); //encerra tudo
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Erro na conexao com o cliente", ex);
            logarMensagem("Erro na conexao com o cliente");
        }
    }

    private boolean enviaParaCliente(Object o) throws IOException {
        if (outToCleinte == null && clienteSocket != null) { //precisa checar sempre
            outToCleinte = new ObjectOutputStream(clienteSocket.getOutputStream());
        }

        if (outToCleinte != null) {
            outToCleinte.writeObject(o);
            outToCleinte.flush();

            return true; //se conseguiu
        }

        return false; //se fechou a conexao ou nao conseguiu
    }

    private Object recebeDoCliente() throws IOException, ClassNotFoundException {
        if (inFromCliente == null && clienteSocket != null) { //precisa checar sempre
            inFromCliente = new ObjectInputStream(clienteSocket.getInputStream());
        }

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
        if (clienteSocket != null) {
            clienteSocket.close();
        }

        isAlive = false;
        interrupt();
    }

    private void logarMensagem(String msg) {
        logMensagens.setText(logMensagens.getText() + "TCP: " + msg + "\n");
    }

    public boolean isIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
}
