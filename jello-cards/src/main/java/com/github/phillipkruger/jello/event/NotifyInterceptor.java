package com.github.phillipkruger.jello.event;

import com.github.phillipkruger.jello.Card;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.logging.Level;
import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import lombok.extern.java.Log;

/**
 * Intercept a method and send a CDI event.
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Notify
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Log
public class NotifyInterceptor implements Serializable {

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {
        Card card = (Card)ic.proceed();
        
        Method method = ic.getMethod();
        String name = method.getName();
        Notify annotation = method.getAnnotation(Notify.class);
        
        ChangeEvent changeEvent = new ChangeEvent(card, annotation.value());
        log.log(Level.INFO, "Intercepting [{0}] : {1}", new Object[]{name, changeEvent});
        broadcaster.fireAsync(changeEvent);
        
        return card;
    }
    
    @Inject
    private Event<ChangeEvent> broadcaster;
}
