/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.controller;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class Fibonacci {

    private static int[] fibonacci;
    private int k = 1;
    private final int limite;

    /**
     * initialize to limite value of the serie
     *
     * @param limite
     */
    public Fibonacci(int limite) {
        fibonacci = new int[limite + 1];
        this.limite = limite;
        fibonacciSerie(limite);
    }

    /**
     * Recursive solution for fibonacci serie
     *
     * @param i
     * @return
     */
    private long fibonacciSerie(int i) {
        if (i < 0) {
            return fibonacci[0];
        } else {
            if (k < 3) {
                fibonacci[i] = k - 1;
                k++;
            } else {
                fibonacci[i] = fibonacci[i + 1] + fibonacci[i + 2];
            }
            return fibonacciSerie(i - 1);
        }
    }

    /**
     * return the value at pos or null if has not been started at this pos
     *
     * @param pos
     * @return
     */
    public synchronized Integer getPos(int pos) {
        if (pos < limite) {
            return fibonacci[limite - pos];
        } else {
            return null;
        }
    }

    public int getLimite() {
        return limite;
    }

}
