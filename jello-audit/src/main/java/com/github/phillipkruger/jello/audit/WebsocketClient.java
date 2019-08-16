package com.github.phillipkruger.jello.audit;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import lombok.extern.java.Log;

/**
 * Create a connection to the server
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationScoped
@Log
public class WebsocketClient {
    
    private Session session;
    
    private String url = "ws://localhost:8080/audit";
    
    public void connect() throws DeploymentException, IOException{
        if(isConnected()){
            log.log(Level.WARNING, "Already connected to [{0}]...", url);
        }else{
            log.log(Level.INFO, "Connecting to [{0}]...", url);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(MessageClientEndpoint.class, URI.create(url));
        }
            
    }
    
    public void disconnect(){
        if(isConnected()){
            try {
                this.session.close();
                log.log(Level.WARNING, "Connection to [{0}] closed", url);
            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean isConnected(){
        return session!=null && session.isOpen();
    }
}
