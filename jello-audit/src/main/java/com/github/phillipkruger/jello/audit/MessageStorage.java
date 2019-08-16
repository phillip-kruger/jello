package com.github.phillipkruger.jello.audit;

import java.util.Stack;
import java.util.logging.Level;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * Store all message
 * @author Phillip Kruger (phillip.kruger@phillip-kruger.com)
 */
@ApplicationScoped
@Log
public class MessageStorage {
    
    @Getter
    private final Stack<String> messagesStack = new Stack<>();
    
    public void store(@Observes String message){
        messagesStack.push(message);
        log.log(Level.INFO, "================ NEW MESSAGE ===================\n{0}", messagesStack.peek());
    }
    
    
    
}
