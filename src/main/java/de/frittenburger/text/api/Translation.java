package de.frittenburger.text.api;

import java.util.ArrayList;
import java.util.List;

public class Translation {

	private List<TranslationPart> parts = null;

	public List<TranslationPart> getParts() {
		return parts;
	}

	public void setParts(List<TranslationPart> parts) {
		this.parts = parts;
	}
	
	public void addPartsItem(TranslationPart part) {
		if(parts == null)
			parts = new ArrayList<TranslationPart>();
		parts.add(part);
	}

}
