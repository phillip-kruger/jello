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
 * Interceptor, Context and Dependency Injection. Intercept a method and send a CDI event.
 * This interceptor look for a card object, firstly as a return object, or failing that
 * a input parameter. This card is the one that gets included with the notification.
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Notify
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Log
public class NotifyInterceptor implements Serializable {

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {
        Card input = getCardInput(ic.getParameters());
        Object returnObject = ic.proceed();
        Card output = getCardOutput(returnObject);
        notify(ic,input, output);
        return returnObject;
    }
    
    private void notify(InvocationContext ic,Card input,Card output){
        // TODO: Later change to include both ? For comparison ? For now find one
        
        Method method = ic.getMethod();
        String name = method.getName();
        Notify annotation = method.getAnnotation(Notify.class);
        
        
        if(output==null && input ==null){
            // Can not place the annotation on that method...
            log.log(Level.WARNING, "Ignoring invalid @Notify annotation on method [{0}] - "
                    + "method must at least return a Card or have a Card as a input parameter", name);
        }
        Card card = output;
        if(card==null)card=input;
        
        ChangeEvent changeEvent = new ChangeEvent(card, annotation.value());
        broadcaster.fireAsync(changeEvent);
    }
    
    private Card getCardInput(Object[] parameters){
        for(Object param:parameters){
            if(param.getClass().equals(Card.class)){
                return (Card)param;
            }
        }
        return null;
    }
    
    private Card getCardOutput(Object response){
        if(response==null)return null;
        if(response.getClass().equals(Card.class)){
           return (Card)response;
        }
        return null;
    }
    
    @Inject
    private Event<ChangeEvent> broadcaster;
}