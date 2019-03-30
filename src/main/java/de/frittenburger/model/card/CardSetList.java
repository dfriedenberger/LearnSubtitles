package de.frittenburger.model.card;

import java.util.ArrayList;
import java.util.List;

public class CardSetList {

	private List<CardSet> cardSets = null;
	
	public List<CardSet> getCardSets() {
		return cardSets;
	}

	public void setCardSets(List<CardSet> cardSets) {
		this.cardSets = cardSets;
	}

	public void addCardSetsItem(CardSet cardset) {
		if(cardSets == null)
			cardSets = new ArrayList<CardSet>();
		cardSets.add(cardset);
	}
	
}
