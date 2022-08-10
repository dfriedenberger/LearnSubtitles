package de.frittenburger.model.vocabulary;

import java.util.HashMap;
import java.util.Map;

public class VocabularyCollection {

	private Map<String,VocabularyList> items;

	public Map<String,VocabularyList> getItems() {
		return items;
	}

	public void setItems(Map<String,VocabularyList> items) {
		this.items = items;
	}
	
	public void addVocabulary(String lang,Vocabulary vocabulary) {

		if(items == null)
			items = new HashMap<>();
		
		if(!items.containsKey(lang))
			items.put(lang,new VocabularyList());
		items.get(lang).addItem(vocabulary);
	
	}
}
