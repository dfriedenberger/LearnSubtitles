package de.frittenburger.model;

import java.util.List;

public class AnnotatedText {

	private String text;
	private List<Annotated> annotated;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Annotated> getAnnotated() {
		return annotated;
	}
	public void setAnnotated(List<Annotated> annotated) {
		this.annotated = annotated;
	}

}
