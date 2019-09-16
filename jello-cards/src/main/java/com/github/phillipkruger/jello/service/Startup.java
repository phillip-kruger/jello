package com.github.phillipkruger.jello.service;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.Comment;
import com.github.phillipkruger.jello.Swimlane;
import java.time.LocalDateTime;
import java.time.Month;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import lombok.extern.java.Log;

/**
 * Generating some test cards on startup
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@ApplicationScoped
public class Startup {

    @PersistenceContext(name="com.github.phillipkruger.cards")
    private EntityManager em;
    
    @Transactional
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init){
        log.info("===== Generating some test cards for demo ====");
    
        Card c1 = generateDummyCard(PHILLIP,D1,"Presentation","Prepare slides for Oracle One presentation",Swimlane.release,
                generateComment(ED,D2,"Should we also add some documentation in GitHub README ?"),
                generateComment(CHARMAINE,D3,"Good Idea Ed, I'll create a card for that"));
        em.persist(c1);
        
        Card c2 = generateDummyCard(ED,D4,"Cloud","Deploy this onto Azure",Swimlane.development,
                generateComment(PHILLIP,D5,"Hi Ed, I am also going to deploy this to OpenShift"));
        em.persist(c2);
        
        Card c3 = generateDummyCard(CHARMAINE,D6,"Docker","Create a docker file for Jello",Swimlane.development,
                generateComment(PHILLIP,D7,"We can use this for both Azure and OpenShift tasks"));
        em.persist(c3);
    
        Card c4 = generateDummyCard(PHILLIP,D8,"OpenShift","Deploy onto OpenShift",Swimlane.pipeline,
                generateComment(PHILLIP,D9,"As discussed on the Azure card"));
        em.persist(c4);
        
        Card c5 = generateDummyCard(PHILLIP,D10,"Documentation in README","Add some documentation",Swimlane.release);
        em.persist(c4);
    }
    
    private Card generateDummyCard(String by,LocalDateTime at,String title,String desc,Swimlane lane,Comment... comment){
        Card card = new Card();
        card.setCreatedBy(by);
        card.setCreated(at);
        card.setDescription(desc);
        card.setSwimlane(lane);
        card.setTitle(title);
        for(Comment c:comment){
             card.addComment(c);
        }
        return card;
    }
    
    private Comment generateComment(String by, LocalDateTime at, String line){
        Comment comment = new Comment();
        comment.setComment(line);
        comment.setMadeBy(by);
        comment.setMadeOn(at);
        return comment;
    }
    
    private static final LocalDateTime D1 = LocalDateTime.of(2019, Month.SEPTEMBER, 1, 13, 24);
    private static final LocalDateTime D2 = LocalDateTime.of(2019, Month.SEPTEMBER, 2, 17, 4);
    private static final LocalDateTime D3 = LocalDateTime.of(2019, Month.SEPTEMBER, 5, 12, 44);
    private static final LocalDateTime D4 = LocalDateTime.of(2019, Month.SEPTEMBER, 5, 23, 01);
    private static final LocalDateTime D5 = LocalDateTime.of(2019, Month.SEPTEMBER, 6, 11, 45);
    private static final LocalDateTime D6 = LocalDateTime.of(2019, Month.SEPTEMBER, 7, 18, 51);
    private static final LocalDateTime D7 = LocalDateTime.of(2019, Month.SEPTEMBER, 8, 9, 35);
    private static final LocalDateTime D8 = LocalDateTime.of(2019, Month.SEPTEMBER, 8, 19, 40);
    private static final LocalDateTime D9 = LocalDateTime.of(2019, Month.SEPTEMBER, 9, 17, 05);
    private static final LocalDateTime D10 = LocalDateTime.of(2019, Month.SEPTEMBER, 10, 13, 55);
    
    private static final String PHILLIP = "phillip.kruger@gmail.com";
    private static final String ED = "edburns@microsoft.com";
    private static final String CHARMAINE = "charmaine.kruger@gmail.com";
}
