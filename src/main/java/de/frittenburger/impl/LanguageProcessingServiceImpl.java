package de.frittenburger.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.frittenburger.interfaces.LanguageProcessingService;
import de.frittenburger.interfaces.LanguageWordAnalyser;
import de.frittenburger.model.Annotation;
import de.frittenburger.model.TokenList;

public class LanguageProcessingServiceImpl implements LanguageProcessingService {

	
	Tokenizer tokenizer = new Tokenizer();
	private final LanguageWordAnalyser languageWordAnalyser;
	
	public LanguageProcessingServiceImpl(LanguageWordAnalyser languageWordAnalyser)
	{
		this.languageWordAnalyser = languageWordAnalyser;
	}
	
	@Override
	public List<Annotation> process(TokenList tokens) {
	
		List<Annotation> words = new ArrayList<Annotation>();
		Set<Integer> visited = new HashSet<Integer>();
		
		for(int ix = 0;ix < tokens.size();ix++)
		{
			if(visited.contains(ix)) continue;		
			Annotation annotation = languageWordAnalyser.analyse(tokens,ix);
			if(annotation != null)
			{
				words.add(annotation);
				visited.addAll(annotation.getIndices());
			}
		}

		return words;
	}
	
	
}
