package com.github.phillipkruger.jello.api;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.service.CardService;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Basic SOAP API for Cards
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@WebService
public class CardSoapApi implements CardApi {

    @Inject
    private CardService cardService;
    
    @WebMethod
    @Override
    public Card createCard(@WebParam Card card){
        return cardService.createCard(card);
    }
    
    @WebMethod
    @Override
    public Card updateCard(@WebParam Card card){
        if(card.getId()==null)return createCard(card);
        return cardService.updateCard(card);
    }
    
    @WebMethod
    @Override
    public Card getCard(@WebParam Long id){
        return cardService.getCard(id);
    }
    
    @WebMethod
    @Override
    public void removeCard(@WebParam Long id){
        Card card = getCard(id);
        if(card!=null)cardService.removeCard(card);
    }
}