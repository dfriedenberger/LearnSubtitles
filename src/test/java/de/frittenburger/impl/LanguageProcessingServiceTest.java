package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.frittenburger.interfaces.LanguageProcessingService;
import de.frittenburger.interfaces.LanguageWordAnalyser;
import de.frittenburger.model.Annotation;
import de.frittenburger.model.Token;
import de.frittenburger.model.TokenList;
import de.frittenburger.srt.SrtMergeReader;
import de.frittenburger.srt.SrtMergeReaderWrapper;

public class LanguageProcessingServiceTest {

	@Test
	public void test() throws IOException {
		
		ClassLoader classLoader1 = SpanishDingLanguageWordAnalyser.class.getClassLoader();
		File fileDict = new File(classLoader1.getResource("dict/es-de/dict.txt").getFile());
		File fileVerbs = new File(classLoader1.getResource("dict/es-de/verbs.txt").getFile());

		ClassLoader classLoader2 = getClass().getClassLoader();
		File file = new File(classLoader2.getResource("mrec/merge_de_es.txt").getFile());
		SrtMergeReader reader = new SrtMergeReader(file);
		SrtMergeReaderWrapper wrapper = new SrtMergeReaderWrapper(reader);
		
		
		LanguageWordAnalyser analyser = new SpanishDingLanguageWordAnalyser(new DingDictionary(fileDict),new SpanishVerbService(fileVerbs));
	
		LanguageProcessingService service = new LanguageProcessingServiceImpl(analyser);
		Tokenizer tokenizer = new Tokenizer();

		
		for(int i = 0;i < wrapper.size() && i < 5;i++)
		{
			String text = wrapper.getText(i,"es");

			TokenList tokens = tokenizer.tokenize(text);
			StringBuffer buffer = new StringBuffer();
			for(Token t : tokens)
				buffer.append(t.getText());
			
			System.out.println(buffer);

			Set<Integer> ind = new HashSet<Integer>();
			for(Annotation annotation : service.process(tokens))
			{
				StringBuffer phrase = new StringBuffer();
				for(Integer ix : annotation.getIndices())
				{
					phrase.append(tokens.get(ix).getText() + " ");
					ind.add(ix);
				}
				System.out.println(phrase+"=> "+annotation);
			}
			
			for(int x = 0;x < tokens.size();x++)
			{
				Token t = tokens.get(x);
				if(t.getType() != 0) continue;
				if(ind.contains(x)) continue;

				System.err.println(t.getText());
				
			}
			
			
		}
		
		
		
		
		
		fail("Not yet implemented");
	}

}
