package com.github.phillipkruger.jello.queue;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.event.ChangeEvent;
import com.github.phillipkruger.jello.json.JsonMessage;
import javax.ejb.Stateless;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.security.enterprise.SecurityContext;

/**
 * Messaging, Context and Dependency Injection, Security. Put a Card on the queue
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Stateless
public class CardQueueProducer {

    @Inject
    private JMSContext context;
    
    @Inject
    private Queue queue;
    
    @Inject
    private JsonMessage jsonMessage;
    
    @Inject
    private SecurityContext securityContext;
    
    public void receiveChangeEvent(@ObservesAsync ChangeEvent event) {
    
        Card card = event.getCard();
        String json = jsonMessage.toJsonMessage(card);

        JMSProducer producer = context.createProducer();
        producer.setProperty(ACTION_PROPERTY, event.getType().name());
        producer.setProperty(USER_PROPERTY, getLoggedInUser());
        producer.send(queue, json);
    }
    
    private String getLoggedInUser(){
        if(securityContext!=null && securityContext.getCallerPrincipal()!=null)return securityContext.getCallerPrincipal().getName();
        return "";
    }
    
    private static final String ACTION_PROPERTY = "ChangeEvent";
    private static final String USER_PROPERTY = "User";
}
