package com.github.phillipkruger.jello.audit;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.java.Log;

/**
 * Websocket. Endpoint for the websocket service
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@Startup
@Singleton
@ServerEndpoint(value = "/stream",encoders = {AuditEntryEncoder.class})
public class AuditWebSocket {
    
    private final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    
    @PostConstruct
    public void init(){
        log.info("Jello board websocket ready");
    }
    
    public void createAuditEntry(AuditEntry auditEntry){
        print(auditEntry);
        broadcast(sessions, auditEntry);
    }
    
    @OnOpen
    public void onOpen(Session session){
        sessions.add(session);
        log.log(Level.INFO, "Session joined [{0}]", session.getId());
    }

    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
        log.log(Level.INFO, "Session left [{0}]", session.getId());
    }
    
    private void broadcast(Set<Session> sessions,AuditEntry auditEntry){
        sessions.forEach((s) -> {
            send(s, auditEntry);
        });
    }
    
    private void send(Session session,AuditEntry auditEntry){
        try {
            if (session.isOpen()){
                session.getBasicRemote().sendObject(auditEntry);
            }
        } catch (IOException | EncodeException  ex){
            log.log(Level.SEVERE, null, ex);
        }
    }   
    
    private void print(AuditEntry auditEntry){        
        log.log(Level.INFO, "(WS) AUDIT: {0}", auditEntry);
    }
}