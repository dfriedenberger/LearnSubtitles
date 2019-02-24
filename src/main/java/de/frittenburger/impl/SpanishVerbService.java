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
		String text2 = null;
		int ix2 = -1;
		for(int i = ix+1;i < tokens.size();i++)
		{
			if(tokens.get(i).getType() == Token.SPACE) continue;
			if(tokens.get(i).getType() != Token.WORD) break;
			text2 = tokens.get(i).getText();
			ix2 = i;
			break;
		}
		
		//INDICATIVO Presente
		String verb = testverbext(text,"ir",new String[]{"o","es","e" , "imos","ís","en"});
		if(verb == null)
			verb = testverbext(text,"er",new String[]{"o","es","e" , "imos","ís","en"});
		if(verb == null)
			verb = testverbext(text,"ar",new String[]{"o","as","a" , "amos","áis","an"});
		
		if(verb != null)
		{
			TokenList l = new TokenList();
			l.add(new Token(verb));
			indicies.add(ix);
			return l;
		}
	
		//PARTICIPIO PASADO
		if(text2 != null && ix2 > 1)
		{
		    //reflexive
			verb = testRelexive(text,text2,"irse",new String[]{"o","es","e" , "imos","ís","en"});
			if(verb == null)
				verb = testRelexive(text,text2,"erse",new String[]{"o","es","e" , "imos","ís","en"});
			if(verb == null)
				verb = testRelexive(text,text2,"arse",new String[]{"o","as","a" , "amos","áis","an"});

			if(verb == null)
				verb = testHaber(text,text2,"ir","ido");
		    if(verb == null)
				verb = testHaber(text,text2,"er","ido");
		    if(verb == null)
				verb = testHaber(text,text2,"ar","ado");
		    
			if(verb != null)
			{
				TokenList l = new TokenList();
				l.add(new Token(verb));
				indicies.add(ix);
				indicies.add(ix2);
				return l;		
			}
		}
				
		
		
		return null;
	}

	
	private String testRelexive(String text, String text2, String infext, String[] exts) {
		
		String refl[] = new String[]{"me","te","se" , "nos","os","se"};
		for(int i = 0;i < refl.length;i++)
		{
			if(!text.equals(refl[i])) continue;
			if(!text2.endsWith(exts[i])) break;
					
			String verb = cut(text2,exts[i])+infext;
			if(verbs.contains(verb))
			{
				return verb;			
			}
		}
		return null;
	}


	private String testHaber(String text, String text2, String extInf,
			String participo) {
		
		for(String hab : new String[]{"he","has","ha" , "hemos","habéis","han"})
		{
			if(!text.equals(hab)) continue;
			if(!text2.endsWith(participo)) break;
					
			String verb = cut(text2,participo)+extInf;
			if(verbs.contains(verb))
			{
				return verb;			
			}
		}
		return null;
	}


	private String testverbext(String text,String infext, String[] exts) {
		for(String ext : exts)
		{
			if(text.endsWith(ext))
			{
				String verb = cut(text,ext)+infext;
				if(verbs.contains(verb))
				{
				   return verb;
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
