package com.github.phillipkruger.jello.view;

import com.github.phillipkruger.jello.event.ChangeEvent;
import javax.json.bind.JsonbBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encoder that can encode ChangeEvent to be streamed over WebSockets
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class ChangeEventEncoder implements Encoder.Text<ChangeEvent> {

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public String encode(ChangeEvent changeEvent) throws EncodeException {
        return JsonbBuilder.create().toJson(changeEvent);
    }
    
    @Override
    public void destroy() {}

}