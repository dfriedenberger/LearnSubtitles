package de.frittenburger.impl;

import java.util.ArrayList;
import java.util.List;


import de.frittenburger.interfaces.BaseWordService;
import de.frittenburger.interfaces.Dictionary;
import de.frittenburger.interfaces.LanguageWordAnalyser;
import de.frittenburger.model.Annotation;
import de.frittenburger.model.TokenList;

public class LanguageWordAnalyserImpl implements LanguageWordAnalyser {

	private final BaseWordService baseWordService;
	private final Dictionary dictionary;
	public LanguageWordAnalyserImpl(Dictionary dictionary,BaseWordService baseWordService) {
		this.baseWordService = baseWordService;
		this.dictionary = dictionary;
	}

	
	

	@Override
	public Annotation analyse(TokenList tokens, int ix) {
		
		Annotation a = dictionary.search(tokens,ix);
		if(a != null) return a;
		
		List<Integer> nIndicies = new ArrayList<Integer>();
		TokenList nList = baseWordService.find(tokens,ix,nIndicies);
		if(nList != null)
		{
			a = dictionary.search(nList,0);
			if(a != null)
			{
				a.getIndices().clear();
				a.getIndices().addAll(nIndicies);
				return a;
			}
		}
		
		return null;
	}

	


	




	


}
