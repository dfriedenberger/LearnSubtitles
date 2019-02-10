package de.frittenburger.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.frittenburger.interfaces.Dictionary;
import de.frittenburger.model.Annotation;
import de.frittenburger.model.Token;
import de.frittenburger.model.TokenList;
import de.frittenburger.model.Translation;

public class DingDictionary implements Dictionary {
	private final Map<String,List<Translation>> translations = new HashMap<String,List<Translation>>();

	public DingDictionary(File dict) {
		
		try(DingReader reader = new DingReader(dict))
		{
			while(true)
			{
				Translation translation = reader.next();
				if(translation == null) break;
				TokenList candidate = translation.getCandidate();
				
				Token token = candidate.first();
				if(!translations.containsKey(token.getKey()))
					translations.put(token.getKey(),new ArrayList<Translation>());
				translations.get(token.getKey()).add(translation);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public Annotation search(TokenList tokens, int ix) {
		if(translations.containsKey(tokens.get(ix).getKey()))		
		{
			for(Translation translation : translations.get(tokens.get(ix).getKey()))
			{
				TokenList candidate = translation.getCandidate();
				{
					int i1 = 0;
					int i2 = ix;
					Set<Integer> indicies = new HashSet<Integer>();
					while(true)
					{
						if(i1 == candidate.size()) break;
						if(i2 == tokens.size()) break;
						Token t1 = candidate.get(i1);
						Token t2 = tokens.get(i2);
					
						
						if(t1.getType() == Token.SPACE)
						{
							//ignore 
							i1++;
							continue;
						}
						
						if(t2.getType() == Token.SPACE)
						{
							//ignore 
							i2++;
							continue;
						}
						String txt1 = t1.getText().toLowerCase();
						String txt2 = t2.getText().toLowerCase();
						
						if(!txt1.equals(txt2)) break;
						
						indicies.add(i2);
						
						if(i1 + 1 == candidate.size())
						{
							//match
							Annotation annotation = new Annotation();
							annotation.getInfos().addAll(translation.getAnnotations());
							annotation.getIndices().addAll(indicies);
							return annotation;
						}
						i1++;
						i2++;
					}
					
					
				}
			}
			
			
		}
		return null;		
	}	

}
