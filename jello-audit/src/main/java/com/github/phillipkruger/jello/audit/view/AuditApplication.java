package com.github.phillipkruger.jello.audit.view;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configure the MVC Application
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
//@EmbeddedIdentityStoreDefinition({
//    @Credentials(callerName = "hem", password = "cheese", groups = {"foo"}),
//    @Credentials(callerName = "haw", password = "cheeze", groups = {"foo", "bar"})}
//)
//@DeclareRoles({"foo", "bar"})
@ApplicationPath("/app")
public class AuditApplication extends Application {

}
