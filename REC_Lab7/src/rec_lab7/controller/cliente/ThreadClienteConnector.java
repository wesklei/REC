/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rec_lab7.controller.cliente;

import java.io.IOException;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ThreadClienteConnector extends Thread {

    private final String ip;
    private final Integer port;

    private final Logger logger = Logger.getLogger(ThreadClienteConnector.class.getName());

    public ThreadClienteConnector(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        Socket communicSocket;
        logger.debug("=>Iniciando comunicacao com o servidor ");
        try {

            //estabelece a conexao
            communicSocket = new Socket(ip, port);

            //TODO comunicar
        } catch (IOException ex) {
            logger.error("Erro na conexao do cliente com o servidor", ex);
        }
    }
}
