package com.github.phillipkruger.jello;

import com.github.phillipkruger.jello.adapter.LocalDateTimeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.json.bind.annotation.JsonbDateFormat;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA, JAXB, JSON-B, Bean Validation. Represent a card in a lane on a board
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@NamedQueries({
    @NamedQuery(name = Card.QUERY_FIND_ALL, query = "SELECT c FROM Card c"),
    @NamedQuery(name = Card.QUERY_SEARCH_BY_TITLE, query = "SELECT c FROM Card c WHERE c.title=:title"),
    @NamedQuery(name = Card.QUERY_FIND_ALL_IN_SWIMLANE, query = "SELECT c FROM Card c WHERE c.swimlane=:swimlane")
})
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public class Card implements Serializable {
    private static final long serialVersionUID = -8531040143398374446L;
    
    public static final String QUERY_FIND_ALL = "Card.findAll";
    public static final String QUERY_SEARCH_BY_TITLE = "Card.searchByTitle";
    public static final String QUERY_FIND_ALL_IN_SWIMLANE = "Card.findAllInSwimlane";
    
    @Id @GeneratedValue
    @XmlAttribute(required=true)
    private Long id;
    
    @NotNull @Size(min=2, max=50)
    @XmlAttribute(required=true)
    private String title;
    
    @XmlAttribute(required=false)
    private String description;
    
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @XmlElementRef(type = Comment.class, required = false)
    private List<Comment> comments;
    
    @XmlAttribute(required=false)
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm")
    private LocalDateTime created = LocalDateTime.now();
    
    @XmlAttribute(required=true)
    private String createdBy;
    
    private Swimlane swimlane = Swimlane.pipeline;
    
    public int getNumberOfComments(){
        if(this.comments==null || this.comments.isEmpty())return 0;
        return this.comments.size();
    }
    
    public void addComment(Comment comment){
        if(comments==null)comments = new ArrayList<>();
        comments.add(comment);
    }
}
