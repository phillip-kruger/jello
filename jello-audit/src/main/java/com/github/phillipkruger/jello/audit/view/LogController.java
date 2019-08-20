package com.github.phillipkruger.jello.audit.view;

import java.net.URL;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
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

    @Resource(lookup = "java:global/webSocketEndpointURL")
    private String webSocketEndpointURL;
    
    @Inject
    private Status status;

     @Inject
    private Models models;

    
    @GET
    @View("log.jsp")
    public void getLog(){
        models.put("webSocketEndpointURL", webSocketEndpointURL);
        log.severe(">>>>>>>>>>>>>> webSocketEndpointURL = "  + webSocketEndpointURL);
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