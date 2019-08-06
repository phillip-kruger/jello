package com.github.phillipkruger.jello;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represent a card in a lane on a board
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@NamedQueries({
    @NamedQuery(name = Card.QUERY_FIND_ALL, query = "SELECT c FROM Card c"),
    @NamedQuery(name = Card.QUERY_SEARCH_BY_TITLE, query = "SELECT c FROM Card c WHERE c.title=:title")
})
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public class Card implements Serializable {
    private static final long serialVersionUID = -8531040143398374446L;
    
    public static final String QUERY_FIND_ALL = "Note.findAll";
    public static final String QUERY_SEARCH_BY_TITLE = "Note.searchByTitle";
    
    @Id
    @GeneratedValue
    @XmlAttribute(required=true)
    private Long id;
    
    @NotNull @Size(min=2, max=50)
    @XmlAttribute(required=true)
    private String title;
    
    @XmlAttribute(required=false)
    private String description;
    
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    @XmlElementRef(type = Comment.class, required = false)
    private List<Comment> comments;
}
