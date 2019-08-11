package com.github.phillipkruger.jello.service;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.Swimlane;
import com.github.phillipkruger.jello.event.ChangeEventType;
import com.github.phillipkruger.jello.event.Notify;
import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import lombok.extern.java.Log;

/**
 * Service in front of the data storage
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@RequestScoped
@Log
public class CardService {
    
    @PersistenceContext(name="com.github.phillipkruger.cards")
    private EntityManager em;
    
    @Notify(ChangeEventType.create)
    @Transactional
    public Card createCard(@NotNull Card card) {
        em.persist(card);
        log.log(Level.INFO, "Created card [{0}]", JsonbBuilder.create().toJson(card));
        return card;
    }
    
    @Notify(ChangeEventType.retrieve)
    public Card getCard(@NotNull @Min(value = 0L) Long id) {
        return em.find(Card.class, id);
    }
    
    public List<Card> searchCards(@NotNull @Size(min=2, max=50) String title) {
        List<Card> cards = (List<Card>)em.createNamedQuery(Card.QUERY_SEARCH_BY_TITLE,Card.class)
					.setParameter("title", title)
					.getResultList();
        log.log(Level.INFO, "Retrieving cards [{0}]", cards);
        return cards;
    }

    @Notify(ChangeEventType.delete)
    @Transactional
    public Card removeCard(@NotNull @Min(value = 0L) Long id) {
        Card card = getCard(id);
        em.remove(card);
        log.log(Level.INFO, "Removing card [{0}]", JsonbBuilder.create().toJson(card));
        return card;
    }

    @Notify(ChangeEventType.update)
    @Transactional
    public Card updateCard(@NotNull Card card) {
        card = em.merge(card);
        log.log(Level.INFO, "Updated card [{0}]", JsonbBuilder.create().toJson(card));
        return card;
    }
    
    public List<Card> getAllCards() {
        return em.createNamedQuery(Card.QUERY_FIND_ALL, Card.class).getResultList();
    }

    public List<Card> getAllCardsInSwimlane(Swimlane swimlane) {
        List<Card> cards = (List<Card>)em.createNamedQuery(Card.QUERY_FIND_ALL_IN_SWIMLANE,Card.class)
					.setParameter("swimlane", swimlane)
					.getResultList();
        
        return cards;
    }
}