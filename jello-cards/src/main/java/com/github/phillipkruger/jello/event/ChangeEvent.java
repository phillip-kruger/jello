package com.github.phillipkruger.jello.event;

import com.github.phillipkruger.jello.Card;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
  * Object representing a change
  * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class ChangeEvent {
    @NotNull
    private Card card;
    @NotNull
    private ChangeEventType type;
}
