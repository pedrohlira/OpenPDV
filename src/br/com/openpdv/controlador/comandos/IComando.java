package br.com.openpdv.controlador.comandos;

import br.com.openpdv.modelo.core.OpenPdvException;

/**
 * Interface dos comandos usados pelos controladores.
 *
 * @author Pedro H. Lira
 */
public interface IComando {

    /**
     * Metodo que executa a acao.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void executar() throws OpenPdvException;

    /**
     * Metodo que desfaz o comando executado.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void desfazer() throws OpenPdvException;
}
