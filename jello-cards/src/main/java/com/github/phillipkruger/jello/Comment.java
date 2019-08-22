package com.github.phillipkruger.jello;

import com.github.phillipkruger.jello.adapter.LocalDateTimeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA, JAXB, JSON-B, Bean Validation. Represent a comment on a card
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public class Comment implements Serializable {
    private static final long serialVersionUID = -8531040143398373846L;
    
    @Id
    @GeneratedValue
    @XmlAttribute(required=true)
    private Long id;
    
    @NotNull
    @XmlAttribute(required=true)
    private String comment;
    
    @XmlAttribute(required=false)
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm")
    private LocalDateTime madeOn = LocalDateTime.now();
    
    @XmlAttribute(required=false)
    private String madeBy;
}