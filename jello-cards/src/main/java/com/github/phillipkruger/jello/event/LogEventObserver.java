package com.github.phillipkruger.jello.event;

import javax.enterprise.event.ObservesAsync;
import lombok.extern.java.Log;

/**
 * Log the events to output
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
public class LogEventObserver {
 
    public void onEvent(@ObservesAsync ChangeEvent event) {
        log.warning(">>>> " + event.getType() + " : " + event.getCard());
    }    
}
