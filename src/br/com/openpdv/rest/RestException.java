package br.com.openpdv.rest;

import com.sun.jersey.api.Responses;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Classe que representa uma excecao de comunicao Rest.
 *
 * @author Pedro H. Lira
 */
public class RestException extends WebApplicationException {

    /**
     * Contrutor padrao com mensagem e falha NULL.
     */
    public RestException() {
        super();
    }

    /**
     * Contrutor que recebe o evento da falha original.
     *
     * @param cause Falha original.
     */
    public RestException(Throwable cause) {
        super(cause);
    }

    /**
     * Contrutor que recebe uma mensagem e o evento da falha original.
     *
     * @param error String com texto adicional.
     */
    public RestException(String error) {
        super(Response.status(Responses.CLIENT_ERROR).entity(error).build());
    }

    /**
     * Contrutor que recebe um status.
     *
     * @param status o status da resposta.
     */
    public RestException(Status status) {
        super(Response.status(status).build());
    }
}
