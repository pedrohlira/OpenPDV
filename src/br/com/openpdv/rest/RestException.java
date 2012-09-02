package br.com.openpdv.rest;

import com.sun.jersey.api.Responses;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class RestException extends WebApplicationException {

    public RestException() {
        super();
    }

    public RestException(Throwable cause) {
        super(cause);
    }

    public RestException(String error) {
        super(Response.status(Responses.CLIENT_ERROR).entity(error).build());
    }

    public RestException(Status status) {
        super(Response.status(status).build());
    }
}
