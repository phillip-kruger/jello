package com.github.phillipkruger.jello.audit;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Indicate a Audit entry
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class AuditEntry {
    private String what;
    private String object;
    private String when;
    private String who;
    private Map<String,Object> metadata;
    
}
