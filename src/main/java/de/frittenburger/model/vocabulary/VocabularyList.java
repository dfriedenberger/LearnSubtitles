package de.frittenburger.model.vocabulary;

import java.util.ArrayList;
import java.util.List;

public class VocabularyList {
	
	private List<Vocabulary> items;

	public List<Vocabulary> getItems() {
		return items;
	}

	public void setItems(List<Vocabulary> items) {
		this.items = items;
	}
	public void addItem(Vocabulary item) {
		if(items == null)
			items = new ArrayList<>();
		items.add(item);
	}
}
