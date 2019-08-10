package com.github.phillipkruger.jello.controller;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.service.CardService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

/**
 * Controller for cards
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@RequestScoped
@Named
public class CardController implements Serializable {
    
    @Inject
    private CardService cardService; 
    
    @Getter
    private List<Card> cards = null;
    
    @Getter @Setter
    private Card newCard = new Card();
    
    @PostConstruct
    public void refresh(){
        cards = cardService.getAllCards();
        this.newCard = new Card();
    }
    
    public void addCard(){
        cardService.createCard(newCard);
        refresh();
    }
    
//    public void updateNote(String title,String text){
//        try {
//            Note note = notesService.getNote(title);
//            note.setText(text);
//            notesService.updateNote(note);
//            refresh();
//        } catch (NoteNotFoundException ex) {
//            // TODO: To Screen
//            log.log(Level.SEVERE, null, ex);
//        }
//    }
    
    public void deleteCard(Long id){
        cardService.removeCard(id);
        refresh();
    }
}