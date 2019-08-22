package com.github.phillipkruger.jello;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Quote POJO
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class Quote {
    private String text;
    private String author;
}
