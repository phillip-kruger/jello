package com.github.phillipkruger.jello.audit.view;

import javax.annotation.security.DeclareRoles;
//import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
//import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configure the MVC Application
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
//@BasicAuthenticationMechanismDefinition(realmName = "Audit")
//@LoginToContinue
@DeclareRoles({"admin", "user"})
@ApplicationPath("/app")
public class AuditApplication extends Application {

}
