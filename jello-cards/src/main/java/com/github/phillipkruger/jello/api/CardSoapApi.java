package com.github.phillipkruger.jello.api;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.service.CardService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Basic SOAP API for Cards
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@RequestScoped
@WebService
public class CardSoapApi implements CardApi {

    @Inject
    private CardService cardService;
    
    @WebMethod
    @Override
    public Card createCard(@WebParam Card card){
        card = cardService.createCard(card);
        return card;
    }
    
    @WebMethod
    @Override
    public Card updateCard(@WebParam Card card){
        if(card.getId()==null)return createCard(card);
        card = cardService.updateCard(card);
        return card;
    }
    
    @WebMethod
    @Override
    public Card getCard(@WebParam Long id){
        Card card = cardService.getCard(id);
        return card;
    }
    
    @WebMethod
    @Override
    public void removeCard(@WebParam Long id){
        cardService.removeCard(id);
    }
}