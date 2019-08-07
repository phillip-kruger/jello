package com.github.phillipkruger.jello.queue;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.event.ChangeEvent;
import javax.ejb.Stateless;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import lombok.extern.java.Log;

/**
 * Put a Card on the queue
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@Stateless
public class CardQueueProducer {

    @Inject
    private JMSContext context;
    
    @Inject
    private Queue queue;
    
    //@Inject
    //private NotesMarshaller marshaller;
    
    public void receiveChangeEvent(@ObservesAsync ChangeEvent event) {
    
        Card card = event.getCard();
        byte[] message = card.toString().getBytes();//marshaller.marshall(card);

        JMSProducer producer = context.createProducer();
        producer.setProperty(ACTION_PROPERTY, event.getType());
        producer.send(queue, message);
        log.severe("Added changeEvent to queue " + event);
    }
    
    private static final String ACTION_PROPERTY = "ChangeEvent";
}
