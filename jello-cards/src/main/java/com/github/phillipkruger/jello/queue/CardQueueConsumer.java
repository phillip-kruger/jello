package com.github.phillipkruger.jello.queue;

import java.util.logging.Level;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;

import javax.jms.Message;
import javax.jms.MessageListener;
import lombok.extern.java.Log;

/**
 * Receive a Card message
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@MessageDriven(name="cardQueueMDB", mappedName="java:app/cardQueue",activationConfig =  {
    @ActivationConfigProperty(propertyName = "destinationType",
                              propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination",
            propertyValue = "java:app/cardQueue")
})
public class CardQueueConsumer implements MessageListener {
    
    @Override
    public void onMessage(Message message) {
        try {
            String action = message.getStringProperty(ACTION_PROPERTY);
            String card = message.getBody(String.class);
            print(action,card);
        } catch (JMSException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void print(String action,String object){
        log.log(Level.INFO, "(JMS) AUDIT: {0}| {1}", new Object[]{action, object});
    }
    
    private static final String ACTION_PROPERTY = "ChangeEvent";
}
