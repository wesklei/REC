/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.main;


/**
 *
 * @author zanatta
 */
public class Main {
    public static void main(String argv[]){
       // Conexao conexao = new Conexao();
        //conexao.start();
        SplashScreen splash = new SplashScreen(1000);
        splash.shows();

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainScreen().setVisible(true);
            }
        });
    }
    
}
