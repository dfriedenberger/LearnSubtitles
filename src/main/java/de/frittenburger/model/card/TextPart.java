package de.frittenburger.model.card;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class TextPart {
	
	private List<String> token = null;
	
    @JsonInclude(JsonInclude.Include.NON_NULL) 
	private String annotationId = null;
	
	
	public List<String> getToken() {
		return token;
	}
	
	public void setToken(List<String> token) {
		this.token = token;
	}
	
	public String getAnnotationId() {
		return annotationId;
	}
	public void setAnnotationId(String annotationId) {
		this.annotationId = annotationId;
	}

}
