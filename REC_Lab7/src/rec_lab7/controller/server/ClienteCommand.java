/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rec_lab7.controller.server;

/**
 *
 * @author Wesklei Migliorini <wesklei.m at gmail dt com>
 */
public class ClienteCommand {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ClienteCommand.class.getName());

    /*Cliente solicitacoes*/
    private final String MSG = "MSG";

    private String comando;
    private String parametros;

    public Object parseMensagem(Object fromCliente) {
        Object retorno = null;

        if (fromCliente instanceof String) {

            //instancia e inicializa o necessario
            comando = (String) fromCliente;
            String[] separa = comando.split("\\(");
            this.comando = separa[0];
            this.parametros = separa[1].substring(0, separa[1].length() - 1);//remove o ultimo ')'

            if (comando.equals(MSG)) {
                retorno = null;//obterMSG(); //TODO
            }
        }

        return retorno;
    }

    private String obterMSG() {
        //TODO
        return null;
    }

}
