package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import de.frittenburger.interfaces.LanguageWordAnalyser;

public class LanguageWordAnalyserTest {

	@Test
	public void test() throws IOException {
		
		ClassLoader classLoader = LanguageWordAnalyserImpl.class.getClassLoader();
		File fileDict = new File(classLoader.getResource("dict/es-de/dict.txt").getFile());
		File fileVerbs = new File(classLoader.getResource("dict/es-de/verbs.txt").getFile());

		
		LanguageWordAnalyser analyser = new LanguageWordAnalyserImpl(new DingDictionary(fileDict),new SpanishVerbService(fileVerbs));
		Tokenizer tokenizer = new Tokenizer();
		
		assertNotNull(analyser.analyse(tokenizer.tokenize("venir"), 0));
		assertNotNull(analyser.analyse(tokenizer.tokenize("dejar de fumar"), 0));
		assertNotNull(analyser.analyse(tokenizer.tokenize("vive"), 0));
		assertNotNull(analyser.analyse(tokenizer.tokenize("ha echado"), 0));

	}

}
