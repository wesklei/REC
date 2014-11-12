/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.controller.cliente;

import com.br.rec_lab7.model.comunic.CodigoErro;
import com.br.rec_lab7.model.comunic.Mensagem;
import com.br.rec_lab7.model.comunic.TipoMensagem;
import java.util.Random;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ClienteCommand {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ClienteCommand.class.getName());

    /*Cliente solicitacoes*/
    private final String FIBONACCI = "FIBONACCI";
    private final String RETORNO_FIBONACCI = "RETORNO_FIBONACCI";

    private String comando;
    private String parametros;

    public String parseMensagem(Object fromServer) {
        String retorno = null;

        if (fromServer instanceof Mensagem) {

            //instancia e inicializa o necessario
            Mensagem msg = (Mensagem) fromServer;
            comando = (String) msg.getDados();

            switch (msg.getTipoMensagem()) {
                case SOLICITACAO: //servidor nao faz solicitacao
                case RETORNO: //retorno do servidor
                    String[] separa = comando.split("\\(");
                    this.comando = separa[0];
                    this.parametros = separa[1].substring(0, separa[1].length() - 1);//remove o ultimo ')'
                    
                    if (comando.equals(RETORNO_FIBONACCI)) { //se foi o fibonacci deu certo
                        retorno = "SUCESSO: " + msg.toString(); //retorna o que recebeu
                    }
                    break;
                case ERRO:
                    retorno = "ERRO: " + msg.toString(); //retorna o que recebeu
                    break;
                default:
                    retorno = "DESCONHECIDO: " + msg.toString();//nao deve acontecer, mais para garantir
            }
        }

        return retorno;
    }

    public Mensagem buildMessage(int limite, int codigo, String identificador) {
        Integer fibo = new Random().nextInt(limite);

        Mensagem retorno = new Mensagem();

        retorno.setCodigo(codigo);
        retorno.setIdentificador(identificador);
        retorno.setTipoMensagem(TipoMensagem.SOLICITACAO);
        retorno.setDados(FIBONACCI + "(" + fibo.toString() + ")");
        return retorno;
    }

}
