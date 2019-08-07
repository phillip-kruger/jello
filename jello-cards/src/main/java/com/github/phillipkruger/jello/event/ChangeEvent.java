package com.github.phillipkruger.jello.event;

import com.github.phillipkruger.jello.Card;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
  * Object representing a change
  * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Data @NoArgsConstructor @AllArgsConstructor
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public class ChangeEvent {
    @NotNull @XmlElementRef
    private Card card;
    @NotNull @XmlAttribute
    private String type;
}
