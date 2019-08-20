package com.github.phillipkruger.jello.api;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Activate JAX-RS.
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationPath("/api")
@DeclareRoles({ "user", "admin" })
public class ApplicationConfig extends Application {

}