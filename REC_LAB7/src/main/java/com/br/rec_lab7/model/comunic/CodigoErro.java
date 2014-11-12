/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.model.comunic;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public enum CodigoErro {

    DESCONHECIDO(1), SEQUENCIA_INVALIDA(2);

    private final int cod;

    CodigoErro(int cod) {
        this.cod = cod;
    }

    public int getCod_Erro() {
        return cod;
    }

    @Override
    public String toString() {
        switch (this) {
            case DESCONHECIDO:
                return "Desconhecido";
            case SEQUENCIA_INVALIDA:
                return "Sequencia Invalida";
            default:
                return "CodigoErro nao identificado: " + cod;
        }

    }
}
