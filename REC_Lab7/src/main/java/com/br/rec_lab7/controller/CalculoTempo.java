/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.controller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class CalculoTempo {

    private static final List<Long> tempos = new ArrayList<Long>();

    public synchronized static void addNovoTempo(long duracao) {
        tempos.add(duracao);
    }
    
    public synchronized static void zerarTempo(){
        tempos.clear();
    }

    public synchronized static long getMedia() {
        Long sum = 0L;
        for (Long l : tempos) {
            sum += l;
        }

        Long mean = tempos.size() > 0 ? sum / tempos.size() : 0;

        return mean;
    }

}
