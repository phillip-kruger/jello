package com.github.phillipkruger.jello.audit.view;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Keeping status of the App (Over all instances)
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Named("status")
@ApplicationScoped
@Data @NoArgsConstructor @AllArgsConstructor
public class Status implements Serializable {
    
    private boolean connected = false;
}
