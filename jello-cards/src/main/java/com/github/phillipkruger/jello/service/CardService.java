package com.github.phillipkruger.jello.service;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.Comment;
import com.github.phillipkruger.jello.cache.CardCacheKeyGenerator;
import com.github.phillipkruger.jello.event.ChangeEventType;
import com.github.phillipkruger.jello.event.Notify;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.SecurityContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import lombok.extern.java.Log;

/**
 * Persistence, Contexts and Dependency Injection, Security, JCache. Service in front of the data storage
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Stateless // - Then @RolesAllowed works
@Dependent
//@RequestScoped // Here, out of the box, @RolesAllowed does not work (yet)
@Log
@DeclareRoles({"admin","user"})
@CacheDefaults(cacheName = "cardCache")
public class CardService {
    
    @PersistenceContext(name="com.github.phillipkruger.cards")
    private EntityManager em;
    
    @Inject
    private SecurityContext securityContext;
    
    @Notify(ChangeEventType.create)
    //@Transactional
    @CachePut( cacheKeyGenerator = CardCacheKeyGenerator.class)
    @RolesAllowed("user")
    public Card createCard(@NotNull @CacheValue Card card) {
        decorateWithUser(card);
        em.persist(card);
        log.log(Level.INFO, "Created card [{0}]", JsonbBuilder.create().toJson(card));
        return card;
    }
    
    @CacheResult
    @RolesAllowed("user")
    public Card getCard(@NotNull @Min(value = 0L) @CacheKey Long id) {
        log.log(Level.INFO, "Retrieving cards with Id [{0}]", id);
        return em.find(Card.class, id);
    }
    
    @RolesAllowed("user")
    public List<Card> searchCards(@NotNull @Size(min=2, max=50) String title) {
        List<Card> cards = (List<Card>)em.createNamedQuery(Card.QUERY_SEARCH_BY_TITLE,Card.class)
					.setParameter("title", title)
					.getResultList();
        log.log(Level.INFO, "Retrieving cards [{0}]", cards);
        return cards;
    }

    @Notify(ChangeEventType.delete)
    //@Transactional
    @CacheRemove(cacheKeyGenerator = CardCacheKeyGenerator.class)
    @RolesAllowed("admin")
    public void removeCard(@NotNull Card card) {
        if (!em.contains(card))card = em.merge(card);
        em.remove(card);
        log.log(Level.INFO, "Removing card [{0}]", JsonbBuilder.create().toJson(card));
    }
    
    @Notify(ChangeEventType.update)
    //@Transactional
    @CachePut(cacheKeyGenerator = CardCacheKeyGenerator.class)
    @RolesAllowed("user")
    public Card updateCard(@NotNull @CacheValue Card card) {
        decorateWithUser(card);
        card = em.merge(card);
        log.log(Level.INFO, "Updated card [{0}]", JsonbBuilder.create().toJson(card));
        return card;
    }
    
    @RolesAllowed("user")
    public List<Card> getAllCards() {
        return em.createNamedQuery(Card.QUERY_FIND_ALL, Card.class).getResultList();
    }

    private void decorateWithUser(Card card){
        if(securityContext.getCallerPrincipal()!=null){
            String username = securityContext.getCallerPrincipal().getName();
        
            if(card.getCreatedBy()==null || card.getCreatedBy().isEmpty()){
                card.setCreatedBy(username);
            }

            if(card.getComments()!=null && !card.getComments().isEmpty()){
                for(Comment comment : card.getComments()){
                    if(comment.getMadeBy()==null || comment.getMadeBy().isEmpty()){
                        comment.setMadeBy(username);
                    }
                }
            }
        }
    }
}