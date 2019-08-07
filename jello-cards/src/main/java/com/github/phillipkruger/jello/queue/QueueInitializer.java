package com.github.phillipkruger.jello.queue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.jms.JMSDestinationDefinition;
import javax.jms.Queue;
import lombok.extern.java.Log;

/**
 * Create the queue and expose as CDI resource
 * @author Phillip Kruger (phillip.kruger@phillip-kruger.com)
 */
@Log
@JMSDestinationDefinition(
    name="java:app/cardQueue",
    interfaceName="javax.jms.Queue",
    destinationName="cardQueue")
public class QueueInitializer {
    
    @Produces
    public Queue exposeQueue() {
        return this.queue;
    }
    
    @Resource(lookup = "java:app/cardQueue")
    private Queue queue;

}
