/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rec_lab7.controller.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import rec_lab7.controller.server.ThreadClienteCommunic;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ServerListener extends Thread{
    
    private static final Logger logger = Logger.getLogger(ServerListener.class.getName());

    private final Integer port;
    
    public ServerListener(Integer port){
        this.port = port;
    }
    
    
    @Override
    public void run() {
        try {
            ServerSocket welcomeSocket = new ServerSocket(port);            
            logger.debug("=>Iniciando thread de escuta para comunicacao com o cliente ...");

            while (true) {

                Socket connectionSocket = welcomeSocket.accept();
                if (connectionSocket != null) {
                    ThreadClienteCommunic threadCliente = new ThreadClienteCommunic(connectionSocket);
                    threadCliente.start();
                }
            }
        } catch (IOException ex) {
            logger.log(Level.ERROR, null, ex);
        }

    }
}
