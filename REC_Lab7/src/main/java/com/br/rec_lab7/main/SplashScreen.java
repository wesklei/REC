/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class SplashScreen extends JWindow {

    private final int duration;

    public SplashScreen(int d) {
        duration = d;
    }

    public void shows() {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.white);

        int width = 800;
        int height = 600;

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);
        URL imgUrl = getClass().getResource("/splash.png");
                
        ImageIcon icon = new ImageIcon(imgUrl);       
        JLabel label = new JLabel(icon);
        label.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                 dispose();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        content.add(label, BorderLayout.CENTER);
        
        setVisible(true);

        try {
            Thread.sleep(duration);
        } catch (InterruptedException ex) {
            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        setVisible(false);
    }

    public void showAndExit() {
        shows();
        System.exit(0);
    }

    public static void main(String[] args) {
        SplashScreen t = new SplashScreen(1000);
        t.shows();
    }

}
