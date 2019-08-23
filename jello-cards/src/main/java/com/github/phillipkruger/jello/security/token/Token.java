package com.github.phillipkruger.jello.security.token;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Just a basic Token. Consider JWT.
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@NoArgsConstructor @AllArgsConstructor 
@Data
public class Token {
    private String user;
    private Set<String> groups = new HashSet<>();
    
    public void addGroup(String group){
        this.groups.add(group);
    }
}
