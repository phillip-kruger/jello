package com.github.phillipkruger.jello.controller;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.Swimlane;
import com.github.phillipkruger.jello.service.CardService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

/**
 * Controller for cards
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@ViewScoped
@Named
public class CardController implements Serializable {
    
    @Inject
    private CardService cardService; 
    
    @Getter
    private List<Card> cards = new ArrayList<>();
    
    @Getter @Setter
    private Card card = new Card();
    
    @Getter
    private final DashboardModel model = new DefaultDashboardModel();
    private final DashboardColumn pipeline = new DefaultDashboardColumn();
    private final DashboardColumn development = new DefaultDashboardColumn();
    private final DashboardColumn testing = new DefaultDashboardColumn();
    private final DashboardColumn release = new DefaultDashboardColumn();
                
    @PostConstruct
    public void init(){
        model.addColumn(pipeline);
        model.addColumn(development);
        model.addColumn(testing);
        model.addColumn(release);
        
        refresh();
    }
    
    public void refresh(){
        this.card = new Card();
        this.cards = cardService.getAllCards();
        for(Card c:cards){
            if(c.getSwimlane().equals(Swimlane.development)){
                development.addWidget("card_" + c.getId());
            }else if(c.getSwimlane().equals(Swimlane.testing)){
                testing.addWidget("card_" + c.getId());
            }else if(c.getSwimlane().equals(Swimlane.release)){
                release.addWidget("card_" + c.getId());
            }else {
                pipeline.addWidget("card_" + c.getId());
            }
        }
    }
    
    public void addCard(){
        card = cardService.createCard(card);
        Long id = card.getId();
        this.card = new Card();
        this.cards = cardService.getAllCards();
        
        pipeline.addWidget("card_" + id);
    }
    
    public void handleReorder(DashboardReorderEvent event) {
        Swimlane swimlane = getSwimlane(event.getColumnIndex());
        Long cardId = getCardId(event.getWidgetId());
        Card c = changeLanes(cardId,swimlane);
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated", "Card :'" + c.getTitle() + "'");
        addMessage(message);
    }
    
    public void handleClose(CloseEvent event) {
        Long cardId = getCardId(event.getComponent().getId());
        Card c = cardService.removeCard(cardId);
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted", "Card :'" + c.getTitle() + "'");
        addMessage(message);
    }
    
    public void viewCommentsDialog(Card c){
        this.card = c;
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('viewCommentsDialog').show();");
    }
    
    public void editCardDialog(Card c){
        this.card = c;
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('editCardDialog').show();");
    }
    
    public void editCard(){
        card = cardService.updateCard(card);
        String title = card.getTitle();
        this.card = new Card();
        this.cards = cardService.getAllCards();
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated", "Card :'" + title + "'");
        addMessage(message);
    }
    
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    private Card changeLanes(Long cardId, Swimlane newLane){
        Card c = cardService.getCard(cardId);
        c.setSwimlane(newLane);
        return cardService.updateCard(c);
    }
    
    private Swimlane getSwimlane(int index){
        if(index == 1)return Swimlane.development;
        if(index == 2)return Swimlane.testing;
        if(index == 3)return Swimlane.release;
        return Swimlane.pipeline;
    }
    
    private Long getCardId(String widgetId){
        return Long.valueOf(widgetId.split("_")[1]);
    }
}