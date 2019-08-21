package com.github.phillipkruger.jello.security.token;

import java.util.ArrayList;
import java.util.List;
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
    private List<String> groups = new ArrayList<>();
    
    public void addGroup(String group){
        this.groups.add(group);
    }
}
