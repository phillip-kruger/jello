package com.github.phillipkruger.jello.controller;

import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import lombok.Data;

/**
 * Login Controller.
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Named
@RequestScoped
@Data
public class LoginController {
    private String username;
    private String password;


    public void login() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("whiteboard.jsf");
    }
    
}
