package de.frittenburger.model.card;

import java.util.HashMap;
import java.util.Map;

public class CardSet {

	private Map<String,Card> cards = null;

	public Map<String,Card> getCards() {
		return cards;
	}

	public void setCards(Map<String,Card> cards) {
		this.cards = cards;
	}
	
	public void putCardsItem(String language,Card card) {
		if(cards == null)
			cards = new HashMap<String,Card>();
		cards.put(language, card);
	}
	
}
