package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.OpenPdvException;

/**
 * Classe que representa uma exceçao de parametro.
 *
 * @author Pedro H. Lira
 */
public class ParametroException extends OpenPdvException {

    /**
     * @see OpenPdvException#OpenPdvException(String)
     */
    public ParametroException(String message) {
        super(message);
    }

    /**
     * @see OpenPdvException#OpenPdvException(String, Throwable)
     */
    public ParametroException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see OpenPdvException#OpenPdvException(Throwable)
     */
    public ParametroException(Throwable cause) {
        super(cause);
    }

    /**
     * @see OpenPdvException#OpenPdvException()
     */
    public ParametroException() {
        super();
    }
}
