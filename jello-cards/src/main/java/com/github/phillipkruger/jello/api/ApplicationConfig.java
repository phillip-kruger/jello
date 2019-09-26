package com.github.phillipkruger.jello.api;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * RESTFull WebServices. Activate JAX-RS.
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationPath("/api")
@SwaggerDefinition (info = @Info (
                    title = "Jello Service",
                    description = "A simple REST API for Jello",
                    version = "1.0.0",
                    contact = @Contact (
                        name = "Phillip Kruger", 
                        email = "apiee@phillip-kruger.com", 
                        url = "http://phillip-kruger.com"
                    )
                )
            )
@DeclareRoles({ "user", "admin" })
public class ApplicationConfig extends Application {

}