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
    private Card newCard = new Card();
    @Getter @Setter
    private Card selectedCard;
    
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
        
        this.cards = cardService.getAllCards();
        
        for(Card c:cards){
            getCorrectColumn(c.getSwimlane()).addWidget("card_" + c.getId());
        }
    }
    
    public void addCard(){
        Card c = cardService.createCard(newCard);
        
        this.newCard = new Card();
        this.cards = cardService.getAllCards(); // ?
        
        pipeline.addWidget("card_" + c.getId());
    }
    
    public void handleReorder(DashboardReorderEvent event) {
        Swimlane swimlane = getSwimlane(event.getColumnIndex());
        Long cardId = getCardId(event.getWidgetId());
        Card c = changeLanes(cardId,swimlane);
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Moved", "Card :'" + c.getTitle() + "'");
        addMessage(message);
    }
    
    public void handleDialogClose(CloseEvent event) {
        this.newCard = new Card();
        this.selectedCard = null;
    }
    
    public void viewCommentsDialog(Card c){
        this.selectedCard = c;
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('viewCommentsDialog').show();");
    }
    
    public void deleteCardDialog(Card c){
        this.selectedCard = c;
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('deleteCardDialog').show();");
    }
    
     public void deleteCard(){
        getCorrectColumn(selectedCard.getSwimlane()).removeWidget("card_" + selectedCard.getId());
        String title = selectedCard.getTitle();
        cardService.removeCard(selectedCard);
        this.selectedCard = null;
        this.cards = cardService.getAllCards();
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted", "Card :'" + title + "'");
        addMessage(message);
    }
            
    public void editCardDialog(Card c){
        this.selectedCard = c;
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('editCardDialog').show();");
    }
    
    public void editCard(){
        String title = cardService.updateCard(selectedCard).getTitle();
        this.selectedCard = null;
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
    
    private DashboardColumn getCorrectColumn(Swimlane swimlane){
        if(swimlane.equals(Swimlane.development)){
            return development;
        }else if(swimlane.equals(Swimlane.testing)){
            return testing;
        }else if(swimlane.equals(Swimlane.release)){
            return release;
        }else {
            return pipeline;
        }
    }
}