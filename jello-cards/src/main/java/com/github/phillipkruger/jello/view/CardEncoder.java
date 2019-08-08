package com.github.phillipkruger.jello.view;

import com.github.phillipkruger.jello.Card;
import javax.json.bind.JsonbBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encoder that can encode Card to be streamed over WebSockets
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class CardEncoder implements Encoder.Text<Card> {

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public String encode(Card card) throws EncodeException {
        return JsonbBuilder.create().toJson(card);
    }
    
    @Override
    public void destroy() {}

}