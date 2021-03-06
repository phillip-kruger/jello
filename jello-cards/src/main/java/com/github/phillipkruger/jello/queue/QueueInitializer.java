package com.github.phillipkruger.jello.queue;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.jms.JMSDestinationDefinition;
import javax.jms.Queue;
import lombok.extern.java.Log;

/**
 * Messaging, Context and Dependency Injection. Create the queue and expose as CDI resource
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@JMSDestinationDefinition(
    name="java:global/cardQueue",
    interfaceName="javax.jms.Queue",
    destinationName="cardQueue")
public class QueueInitializer {
    
    @Produces
    public Queue exposeQueue() {
        return this.queue;
    }
    
    @Resource(lookup = "java:global/cardQueue")
    private Queue queue;

}
