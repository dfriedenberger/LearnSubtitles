package de.frittenburger.model;

import java.util.List;
import java.util.Map;

public class CardTextCollection {

	
	
	private List<CardTextToken> textToken;

	private Map<String,CardTextAnnotation> textAnnotation;

	public List<CardTextToken> getTextToken() {
		return textToken;
	}

	public void setTextToken(List<CardTextToken> textToken) {
		this.textToken = textToken;
	}

	public Map<String, CardTextAnnotation> getTextAnnotation() {
		return textAnnotation;
	}

	public void setTextAnnotation(Map<String, CardTextAnnotation> textAnnotation) {
		this.textAnnotation = textAnnotation;
	}


	
	

}
