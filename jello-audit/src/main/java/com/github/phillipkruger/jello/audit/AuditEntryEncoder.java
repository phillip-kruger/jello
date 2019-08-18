package com.github.phillipkruger.jello.audit;

import javax.json.bind.JsonbBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encoder that can encode AuditEntry to be streamed over WebSockets
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class AuditEntryEncoder implements Encoder.Text<AuditEntry> {

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public String encode(AuditEntry auditEntry) throws EncodeException {
        return JsonbBuilder.create().toJson(auditEntry);
    }
    
    @Override
    public void destroy() {}

}