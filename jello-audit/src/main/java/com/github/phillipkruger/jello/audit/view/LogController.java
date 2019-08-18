package com.github.phillipkruger.jello.audit.view;

import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.View;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import lombok.extern.java.Log;

/**
 * Controller for the audit log
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@Path("/")
@Controller
public class LogController {

    @Inject
    private Status status;

    @GET
    @View("log.jsp")
    public void getLog(){
        
    }
    
    @POST
    @View("log.jsp")
    public void action(@FormParam("action") String action) {
        if(action.equalsIgnoreCase("connect")){
            this.status.setConnected(true);
        }else if(action.equalsIgnoreCase("disconnect")){
            this.status.setConnected(false);
        }
    }
}