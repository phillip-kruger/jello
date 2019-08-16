package com.github.phillipkruger.jello.audit;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.event.ChangeEvent;
import com.github.phillipkruger.jello.event.ChangeEventType;
import com.github.phillipkruger.jello.service.CardService;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.java.Log;

/**
 * Endpoint for the websocket service
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@Startup
@Singleton
@ServerEndpoint(value = "/audit",encoders = {AuditEntryEncoder.class})
public class AuditWebSocket {
    
    private final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
            
    @Inject 
    private CardService cardService;
    
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
        List<Card> cards = cardService.getAllCards();
        for(Card card : cards){
            sendCard(session,card);
        }
    }

    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
        log.log(Level.INFO, "Session left [{0}]", session.getId());
    }
    
    private void sendCard(Session session,Card card){
        try {
            if (session.isOpen()){
                ChangeEvent ce = new ChangeEvent(card,ChangeEventType.create);
                session.getBasicRemote().sendObject(ce);
            }
        } catch (IOException | EncodeException  ex){
            log.log(Level.SEVERE, null, ex);
        }
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