package de.frittenburger.model.card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Card {

	
	private List<TextPart> textParts = null;
	private Map<String,String> annotations = null;
	
	public List<TextPart> getTextParts() {
		return textParts;
	}
	
	public void setTextParts(List<TextPart> textParts) {
		this.textParts = textParts;
	}
	
	public void addTextPartsItem(TextPart textPart) {
		if(textParts == null)
			textParts = new ArrayList<TextPart>();
		textParts.add(textPart);
	}
	
	public Map<String, String> getAnnotations() {
		return annotations;
	}
	
	public void setAnnotations(Map<String, String> annotations) {
		this.annotations = annotations;
	}
	
	public void putAnnotationsItem(String annotationId,String annotation)
	{
		if(annotations == null)
			annotations = new HashMap<String,String>();
		annotations.put(annotationId, annotation);
	}
}
