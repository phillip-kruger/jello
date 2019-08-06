package com.github.phillipkruger.jello.api;

import com.github.phillipkruger.jello.Card;
import javax.jws.WebService;

@WebService
public interface CardApi {
    public Card createCard(Card card);
    public Card updateCard(Card card);
    public Card getCard(Long id);
    public void removeCard(Long id);
}
