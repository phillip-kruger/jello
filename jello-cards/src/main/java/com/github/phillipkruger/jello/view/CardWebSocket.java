package com.github.phillipkruger.jello.view;

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
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
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
@ServerEndpoint(value = "/card",encoders = {CardEncoder.class,ChangeEventEncoder.class})
public class CardWebSocket {
    
    private final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
            
    @Inject 
    private CardService cardService;
    
    @PostConstruct
    public void init(){
        log.info("Jello board websocket ready");
    }
    
    public void changeEvent(@ObservesAsync ChangeEvent changeEvent){
        broadcastChangeEvent(sessions, changeEvent);
    }
    
    @OnOpen
    public void onOpen(Session session){
        sessions.add(session);
        log.log(Level.INFO, "Session joined [{0}]", session.getId());
        List<Card> cards = cardService.getAllCards();
        log.severe(">>>>> number of cards:" + cards.size());
        
        for(Card card : cards){
            sendCard(session,card);
        }
        log.severe("<<<<<< done");
    }

    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
        log.log(Level.INFO, "Session left [{0}]", session.getId());
    }
    
    @OnMessage
    public void onMessage(String message, Session session){
        if(message!=null && "".equalsIgnoreCase(message)){
            log.severe(">>>>>>>>>>>>>>>>>>>>>>>>>>> " + message);
            //try {
                //Note note = notesService.getNote(message);
                //broadcastCard(session,card);
            //} catch (NoteNotFoundException ex) {
            //    Logger.getLogger(NotesSocket.class.getName()).log(Level.SEVERE, null, ex);
            //}
        }
    }
    
    private void broadcastCard(Session session,Card card){
        Set<Session> openSessions = session.getOpenSessions();
        openSessions.forEach((s) -> {
            sendCard(s, card);
        });
        
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
    
    private void broadcastChangeEvent(Set<Session> sessions,ChangeEvent changeEvent){
        sessions.forEach((s) -> {
            sendChangeEvent(s, changeEvent);
        });
    }
    
    private void sendChangeEvent(Session session,ChangeEvent changeEvent){
        try {
            if (session.isOpen()){
                session.getBasicRemote().sendObject(changeEvent);
            }
        } catch (IOException | EncodeException  ex){
            log.log(Level.SEVERE, null, ex);
        }
    }   
}