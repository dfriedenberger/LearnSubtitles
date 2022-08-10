package de.frittenburger.model.vocabulary;

import java.util.List;

public class Vocabulary {

	private int level;
	private String text;
	private String translation;
	private List<String> quizCandidates;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTranslation() {
		return translation;
	}
	public void setTranslation(String translation) {
		this.translation = translation;
	}
	@Override
	public String toString() {
		return "Vocabulary [text=" + text + ", translation=" + translation
				+ "]";
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<String> getQuizCandidates() {
		return quizCandidates;
	}
	public void setQuizCandidates(List<String> quizCandidates) {
		this.quizCandidates = quizCandidates;
	}
	
	
}
