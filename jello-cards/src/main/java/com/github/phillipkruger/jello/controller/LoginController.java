package com.github.phillipkruger.jello.controller;

import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

/**
 * Login Controller.
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@Named
@RequestScoped
public class LoginController {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String password;
    
    @Inject
    private SecurityContext securityContext;
    
    public String login() throws IOException {
        return "board";
    }
    
    public void logout() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        ((HttpSession) externalContext.getSession(false)).invalidate();
        externalContext.redirect("index.xhtml");
    }
    
    public String getLoggedInUser(){
        if(isLoggedIn())return securityContext.getCallerPrincipal().getName();
        return null;
    }
    
    private boolean isLoggedIn(){
        return securityContext!=null && securityContext.getCallerPrincipal()!=null;
    }
}
