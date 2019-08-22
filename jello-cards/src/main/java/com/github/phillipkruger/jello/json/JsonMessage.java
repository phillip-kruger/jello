package com.github.phillipkruger.jello.json;

import com.github.phillipkruger.jello.Card;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.bind.JsonbBuilder;
import javax.json.stream.JsonGenerator;

/**
 * JSON-P, JSON-B, CDI. Create the correct Json message to be added to the Queue
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationScoped
public class JsonMessage {

    private JsonWriterFactory writerFactory;
    
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        this.writerFactory = Json.createWriterFactory(properties);
    }
    
    public String toJsonMessage(Card card){
        try(StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = writerFactory.createWriter(sw)){
        
            JsonObject json = Json.createObjectBuilder()
                .add("systemId", "Jello")
                .add("systemVersion", "1.0.0")
                .add("timestamp", getTimeStamp())
                .add("card", getCardJsonObject(card))
                .build();

            jsonWriter.write(json);
            return sw.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private JsonObject getCardJsonObject(Card card){
        String json = JsonbBuilder.create().toJson(card);
        final JsonReader reader = Json.createReader(new StringReader(json));
        return reader.readObject();
    }
    
    private String getTimeStamp(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}
