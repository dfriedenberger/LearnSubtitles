package de.frittenburger.text.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;



public class TranslationPart {

	
	private List<String> text = null;
	
    @JsonInclude(JsonInclude.Include.NON_NULL) 
	private String translation = null;
    	
	
	public List<String> getText() {
		return text;
	}

	public void setText(List<String> text) {
		this.text = text;
	}

	public void setTextItem(String token) {
		if(text == null)
			text = new ArrayList<String>();
		text.add(token);
	}

	public String getTranslation() {
		return translation;
	}
	
	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public boolean hasTranslation() {
		return translation != null;
	}
	

}
