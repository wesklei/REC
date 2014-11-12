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
public enum TipoMensagem {

    SOLICITACAO(1), RETORNO(2), ERRO(3);

    private final int tipoMensagem;

    TipoMensagem(int tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }

    public int getTipoMensagem() {
        return tipoMensagem;
    }

    @Override
    public String toString() {
        switch (this) {
            case SOLICITACAO:
                return "Solicitacao";
            case RETORNO:
                return "Retorno";
            case ERRO:
                return "Erro";
            default:
                return "TipoMensagem nao identificado: " + tipoMensagem;
        }

    }
}
