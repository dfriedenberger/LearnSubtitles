package de.frittenburger.model;

import java.util.ArrayList;
import java.util.List;

public class Translation {

	private final TokenList candidate;
	private final List<String> annotations = new ArrayList<String>();
	
	public Translation(TokenList candidate)
	{
		this.candidate = candidate;
	}
	public TokenList getCandidate() {
		return candidate;
	}

	public List<String> getAnnotations() {
		return annotations;
	}

	@Override
	public String toString() {
		return "Translation [candidate=" + candidate + ", annotations="
				+ annotations + "]";
	}


	

}
