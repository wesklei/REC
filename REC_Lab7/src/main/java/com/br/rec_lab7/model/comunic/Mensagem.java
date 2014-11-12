/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.model.comunic;

import java.io.Serializable;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class Mensagem implements Serializable {

    private static final long serialVersionUID = 93261342737677L;

    private int codigo;
    private String identificador;
    private CodigoErro codigoErro;
    private TipoMensagem tipoMensagem;
    private String dados;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public CodigoErro getCodigoErro() {
        return codigoErro;
    }

    public void setCodigoErro(CodigoErro codigoErro) {
        this.codigoErro = codigoErro;
    }

    public TipoMensagem getTipoMensagem() {
        return tipoMensagem;
    }

    public void setTipoMensagem(TipoMensagem tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }

    public String getDados() {
        return dados;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }

    @Override
    public String toString() {
        if (tipoMensagem == TipoMensagem.ERRO) {
            return "Mensagem{" + "TipoMensagem=" + tipoMensagem.toString() + ", CodigoErro=" + codigoErro.toString() + '}';
        } else {
            return "Mensagem{" + "TipoMensagem=" + tipoMensagem.toString() + ", Dados=" + dados + '}';
        }
    }
}
