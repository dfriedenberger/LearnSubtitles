package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.frittenburger.interfaces.BaseWordService;
import de.frittenburger.model.Token;
import de.frittenburger.model.TokenList;

public class SpanishVerbService implements BaseWordService {

	
	
	private final Set<String> verbs = new HashSet<String>();

	public SpanishVerbService(File fileVerbs) throws IOException {
		
		for(String line : Files.readAllLines(fileVerbs.toPath(),StandardCharsets.UTF_8))
		{
			String verb = line.trim();
			if(verb.isEmpty() || verb.startsWith("#")) continue;
			verbs.add(verb);
		}
	}


	@Override
	public TokenList find(TokenList tokens, int ix,List<Integer> indicies) {
		
		
		if(ix >= tokens.size())
			return null;
		
		String text = tokens.get(ix).getText();
		
		//INDICATIVO Presente
		for(String ext : new String[]{"o","es","e" , "imos","ís","en"})
		{
			if(text.endsWith(ext))
			{
				String verb = cut(text,ext)+"ir";
				if(verbs.contains(verb))
				{
					TokenList l = new TokenList();
					l.add(new Token(verb));
					indicies.add(ix);
					return l;
				}
			}
		}
		
		for(String hab : new String[]{"he","has","ha" , "hemos","habéis","han"})
			if(text.equals(hab))
			{
				//PARTICIPIO PASADO
				for(int i = ix+1;i < tokens.size();i++)
				{
					String text2 = tokens.get(i).getText();
					if(text2.endsWith("ado"))
					{
						String verb = cut(text2,"ado")+"ar";
						if(verbs.contains(verb))
						{
							TokenList l = new TokenList();
							l.add(new Token(verb));
							indicies.add(ix);
							indicies.add(i);
							return l;			
						}
					}
				}
				
			}
		
		
		
		return null;
	}

	
	public String find(String text) {

		//PARTICIPIO PASADO
		if(text.endsWith("ado"))
			return cut(text,"ado")+"ar";
		
		
		
		return null;
	}

	private String cut(String text, String post) {
		int l = text.length();
		int s = post.length();
		return text.substring(0, l - s);
	}

	
	
}
