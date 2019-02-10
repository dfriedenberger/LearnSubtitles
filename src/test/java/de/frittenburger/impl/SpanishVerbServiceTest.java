package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.frittenburger.interfaces.BaseWordService;

public class SpanishVerbServiceTest {

	@Test
	public void test() throws IOException {
		
		ClassLoader classLoader = SpanishDingLanguageWordAnalyser.class.getClassLoader();

		File fileVerbs = new File(classLoader.getResource("dict/es-de/verbs.txt").getFile());

		BaseWordService service = new SpanishVerbService(fileVerbs);
		
		Tokenizer tokenizer = new Tokenizer();
		
		
		assertNull(service.find(tokenizer.tokenize(""), 0, new ArrayList<Integer>()));
		assertNotNull(service.find(tokenizer.tokenize("vivo"), 0, new ArrayList<Integer>()));
		assertNotNull(service.find(tokenizer.tokenize("vive"), 0, new ArrayList<Integer>()));
		assertNotNull(service.find(tokenizer.tokenize("vives"), 0, new ArrayList<Integer>()));

		assertNotNull(service.find(tokenizer.tokenize("he echado"), 0, new ArrayList<Integer>()));
		assertNotNull(service.find(tokenizer.tokenize("has echado"), 0, new ArrayList<Integer>()));
		assertNotNull(service.find(tokenizer.tokenize("ha echado"), 0, new ArrayList<Integer>()));

		
		
	}

}
