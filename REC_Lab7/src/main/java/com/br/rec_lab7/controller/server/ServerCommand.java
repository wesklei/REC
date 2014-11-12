/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.rec_lab7.controller.server;

import com.br.rec_lab7.model.comunic.CodigoErro;
import com.br.rec_lab7.model.comunic.Mensagem;
import com.br.rec_lab7.model.comunic.TipoMensagem;
import com.br.rec_lab7.view.ServerManager;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ServerCommand {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ServerCommand.class.getName());

    /*Cliente solicitacoes*/
    private final String FIBONACCI = "FIBONACCI";
    private final String RETORNO_FIBONACCI = "RETORNO_FIBONACCI";

    private String comando;
    private String parametros;

    public Object parseMensagem(Object fromCliente) {
        Object retorno = null;

        if (fromCliente instanceof Mensagem) {

            //instancia e inicializa o necessario
            Mensagem msg = (Mensagem) fromCliente;

            switch (msg.getTipoMensagem()) {
                case SOLICITACAO:

                    comando = (String) msg.getDados();
                    String[] separa = comando.split("\\(");
                    this.comando = separa[0];
                    this.parametros = separa[1].substring(0, separa[1].length() - 1);//remove o ultimo ')'

                    if (comando.equals(FIBONACCI)) {
                        retorno = obterFibonacci(msg, Integer.valueOf(parametros));
                    }
                    break;
                case RETORNO: //cliente nao retorna nada
                case ERRO: //descarta mensagem de erro
            }
        }

        return retorno;
    }

    private Mensagem obterFibonacci(Mensagem fromCliente, int posicao) {
        Integer fibo = ServerManager.getFibonacciPos(posicao);

        Mensagem retorno = new Mensagem();

        retorno.setCodigo(fromCliente.getCodigo());
        retorno.setIdentificador(fromCliente.getIdentificador());

        if (fibo != null) {
            retorno.setTipoMensagem(TipoMensagem.RETORNO);
            retorno.setDados(RETORNO_FIBONACCI + "(" + fibo.toString() + ")");
        } else {
            retorno.setTipoMensagem(TipoMensagem.ERRO);
            retorno.setCodigoErro(CodigoErro.SEQUENCIA_INVALIDA);
        }

        return retorno;
    }

}
