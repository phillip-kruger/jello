package com.github.phillipkruger.jello.audit;

import java.io.IOException;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.websocket.ClientEndpoint;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import lombok.extern.java.Log;

/**
 * The endpoint that receive messages
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Dependent
@ClientEndpoint
@Log
public class MessageClientEndpoint {

    @Inject
    private WebsocketClient websocketClient;
    
    @Inject
    private Event<String> broadcaster;
    
    @OnMessage 
    public void onMessage(String message) {
        broadcaster.fire(message);
    }

    @OnClose
    public void onClose(Session session){
        try {
            log.severe("Lost connection to the server, trying to reconnect ...");
            websocketClient.connect();
        } catch (DeploymentException | IOException ex) {
            log.severe(ex.getMessage());
        }
    }
    
}