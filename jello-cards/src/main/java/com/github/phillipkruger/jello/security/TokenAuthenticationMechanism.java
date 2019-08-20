package com.github.phillipkruger.jello.security;

import java.util.Arrays;
import java.util.HashSet;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

/**
 * Token based authentication
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationScoped
@Log
public class TokenAuthenticationMechanism {//implements HttpAuthenticationMechanism {

//    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        log.severe("MY-API-KEY = "  + request.getHeader("MY-API-KEY"));
        if (request.getHeader("MY-API-KEY") != null) {

            final String key = request.getHeader("MY-API-KEY");
            if (key != null && key.equalsIgnoreCase("DUKE ROCKS")) {

                return httpMessageContext.notifyContainerAboutLogin(
                        "app", new HashSet<>(Arrays.asList("user")));
            } else {
                return httpMessageContext.responseUnauthorized();
            }
        }
        
        return httpMessageContext.doNothing();
    }
}