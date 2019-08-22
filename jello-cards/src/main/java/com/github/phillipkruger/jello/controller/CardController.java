package com.github.phillipkruger.jello.controller;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.Comment;
import com.github.phillipkruger.jello.Swimlane;
import com.github.phillipkruger.jello.security.token.TokenHelper;
import com.github.phillipkruger.jello.service.CardService;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
 * JSF. Controller for cards
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@ViewScoped
@Named
public class CardController implements Serializable {
    
    @Inject
    private TokenHelper tokenHelper;
    
    @Inject
    private CardService cardService; 
    
    @Getter
    private List<Card> cards = new ArrayList<>();
    
    @Getter @Setter
    private Card newCard;
    
    @Getter @Setter
    private Comment newComment;
    
    @Getter @Setter
    private Card selectedCard;
    
    @Getter
    private String apiKey;
    
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
        
        try {
            this.apiKey = tokenHelper.generateToken();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            addWarningMessage("api_key_gen_error",ex.getMessage());
        }
    }
    
    
    // API Key
    public void viewApiKeyDialog(){
        showDialog("apiKeyDialog");
    }
    
    // Create
    public void viewCreateDialog(){
        reset();
        this.newCard = new Card();
        showDialog("addCardDialog");
    }
    
    public void addCard(){
        Card c = cardService.createCard(newCard);
        reset();
        this.cards.add(c);
        pipeline.addWidget("card_" + c.getId());
    }
    
    // Edit
    public void editCardDialog(Card c){
        checkSwimlane(c);
        this.selectedCard = c;
        showDialog("editCardDialog");
    }
    
    public void editCard(){
        String title = cardService.updateCard(selectedCard).getTitle();
        this.selectedCard = null;
        addInfoMessage("updated_card",title);
    }
    
    // Update (lane)
    public void handleReorder(DashboardReorderEvent event) {
        Swimlane swimlane = getSwimlane(event.getColumnIndex());
        Long cardId = getCardId(event.getWidgetId());
        Card c = changeLanes(cardId,swimlane);
        
        addInfoMessage("moved_card",c.getTitle());
    }
    
    // Update (comments)
    public void viewCommentsDialog(Card c){
        checkSwimlane(c);
        this.selectedCard = c;
        this.newComment = new Comment();
        showDialog("viewCommentsDialog");
    }
    
    public void addComment(){
        selectedCard.addComment(newComment);
        cardService.updateCard(selectedCard).getTitle();
        String comment = newComment.getComment();
        reset();
        addInfoMessage("comment_added",comment);
    }
    
    // Delete
    public void deleteCardDialog(Card c){
        checkSwimlane(c);
        this.selectedCard = c;
        showDialog("deleteCardDialog");
    }
    
    public void deleteCard(){
        this.cards.remove(selectedCard);
        String title = selectedCard.getTitle();
        cardService.removeCard(selectedCard);
        reset();

        addInfoMessage("deleted_card",title);
    }
    
    // Cancel dialog 
    public void handleDialogClose(CloseEvent event) {
        reset();
    }
    
    private void checkSwimlane(Card c){
        Swimlane original = cardService.getCard(c.getId()).getSwimlane();
        c.setSwimlane(original);
    }
    
    private void reset(){
        this.newCard = null;
        this.newComment = null;
        this.selectedCard = null;
    }
    
    private void showDialog(String dialogId){
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('" + dialogId + "').show();");
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
        switch (swimlane) {
            case development:
                return development;
            case testing:
                return testing;
            case release:
                return release;
            default:
                return pipeline;
        }
    }
    
    private void addWarningMessage(String titleKey,String message){
        addMessage(FacesMessage.SEVERITY_WARN,titleKey,message);
    }
    
    private void addInfoMessage(String titleKey,String message){
        addMessage(FacesMessage.SEVERITY_INFO,titleKey,message);
    }
    
    private void addMessage(FacesMessage.Severity severity, String titleKey,String message){
        String title = getI18n(titleKey);
        FacesMessage facesmessage = new FacesMessage(severity, title, message);
        addMessage(facesmessage);
    }
    
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage("growl", message);
    }
    
    private String getI18n(String key){
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, "#{i18n['" + key + "']}", String.class);
    }
    
}