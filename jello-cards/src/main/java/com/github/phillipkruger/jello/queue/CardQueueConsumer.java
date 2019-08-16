package com.github.phillipkruger.jello.queue;

import com.github.phillipkruger.jello.audit.AuditEntry;
import com.github.phillipkruger.jello.audit.AuditWebSocket;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
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
    
    @Inject
    private AuditWebSocket auditWebSocket;
    
    @Override
    public void onMessage(Message message) {
        try {
            String action = message.getStringProperty(ACTION_PROPERTY);
            String body = message.getBody(String.class);
            
            JsonObject jsonObject = getJsonObject(body);
            AuditEntry auditEntry = toAuditEntry(action, jsonObject);
            auditWebSocket.createAuditEntry(auditEntry);
        } catch (JMSException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private JsonObject getJsonObject(String json){
        final JsonReader reader = Json.createReader(new StringReader(json));
        return reader.readObject();
    }
    
    private AuditEntry toAuditEntry(String action,JsonObject jsonObject){
        AuditEntry auditEntry = new AuditEntry();
        auditEntry.setWhat(action);
        auditEntry.setObject("card");
        
        String timestamp = jsonObject.getString("timestamp");
        auditEntry.setWhen(timestamp);
        
        JsonObject cardJsonObject = jsonObject.getJsonObject("card");
        
        Map<String,Object> metadata = new HashMap<>();
        metadata.put("id", cardJsonObject.getInt("id"));
        metadata.put("title", cardJsonObject.getString("title"));
        metadata.put("description", cardJsonObject.getString("description"));
        metadata.put("created", cardJsonObject.getString("created"));
        metadata.put("numberOfComments", cardJsonObject.getInt("numberOfComments"));
        metadata.put("swimlane", cardJsonObject.getString("swimlane"));
        
        auditEntry.setMetadata(metadata);
        
        return auditEntry;
    }
    
    private static final String ACTION_PROPERTY = "ChangeEvent";
}
