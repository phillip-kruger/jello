package com.github.phillipkruger.jello.api;

import javax.ejb.EJBAccessException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Return a 403 on AccessException 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Provider
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.TEXT_PLAIN})
public class EJBAccessExceptionMapper implements ExceptionMapper<EJBAccessException> {

    @Override
    public Response toResponse(EJBAccessException e) {
        return Response.status(403)
                .header(REASON, e.getMessage()).build();
    }
    
    private static final String REASON = "reason";
}