/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rec_lab7.controller.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ThreadClienteCommunic extends Thread {

    private final Logger logger = Logger.getLogger(ThreadClienteCommunic.class.getName());

    private ObjectOutputStream outToCleinte;
    private ObjectInputStream inFromCliente;
    private final Socket clienteSocket;
   // private final ClienteCommand ClienteCmd;

    public ThreadClienteCommunic(Socket clienteSocket) throws IOException {
        this.clienteSocket = clienteSocket;
       // this.ClienteCmd = new ClienteCommand();
    }

    @Override
    public void run() {
        Object comandoDoCliente;
        Object retornoParaCliente;
        try {

            while (true) {
                logger.debug("Cliente Conectou! Thread de comunicacao com cliente esperando dados ...");

                comandoDoCliente = recebeDoCliente();//le do cliente a solicitacao

                if (comandoDoCliente != null) {
                    if (comandoDoCliente instanceof String) {
                        logger.debug("Comandos do Cliente: " + comandoDoCliente);

                        String mensagemString = (String) comandoDoCliente;
                        //TODO
                        retornoParaCliente = null;//(Object) comandosCliente.parseMensagem(mensagemString);

                        logger.debug("Comandos para o Cliente: " + retornoParaCliente);
                        if (!enviaParaCliente(retornoParaCliente)) {
                            //ocorreu algum problema
                            break;
                        }

                    }else if (inFromCliente.read() == -1) {//conexao fechou
                        logger.debug("Cleinte fechou a conexao");
                        break;
                    } else {
                        logger.debug("Recebeu comando do Cliente null");
                        break;
                    }
                }
            }

            fecharConexao(); //encerra tudo
        } catch (EOFException ex) {
            logger.log(Level.INFO, "Cliente fechou a conexao");
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Erro na conexao com o cliente", ex);
        } catch (ClassNotFoundException ex) {
            logger.log(Level.ERROR, "Erro na conversao do objeto mensagem recebido do ciente", ex);
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
        inFromCliente.close();
        outToCleinte.close();
        clienteSocket.close();

        if (inFromCliente != null) {
            inFromCliente.close();
        }
        if (outToCleinte != null) {
            outToCleinte.close();
        }
        if (clienteSocket != null && clienteSocket.isConnected()) {
            clienteSocket.close();
        }
    }
}
