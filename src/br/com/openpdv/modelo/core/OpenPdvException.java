package br.com.openpdv.modelo.core;

/**
 * Classe que representa uma excecao geral do sistema.
 *
 * @author Pedro H. Lira
 */
public class OpenPdvException extends Exception {

    /**
     * Contrutor padrao com mensagem e falha NULL.
     */
    public OpenPdvException() {
        super();
    }

    /**
     * Contrutor que recebe o evento da falha original.
     *
     * @param cause Falha original.
     */
    public OpenPdvException(Throwable cause) {
        super(cause);
    }

    /**
     * Contrutor que recebe uma mensagem e o evento da falha original.
     *
     * @param message String com texto adicional.
     * @param cause Falha original.
     */
    public OpenPdvException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Contrutor que recebe uma mensagem adicional.
     *
     * @param message String com texto adicional.
     */
    public OpenPdvException(String message) {
        super(message);
    }
}
