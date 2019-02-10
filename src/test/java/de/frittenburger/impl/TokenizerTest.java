package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import de.frittenburger.model.Token;

public class TokenizerTest {

	@Test
	public void test() {
		Tokenizer tokenizer = new Tokenizer();
		
		List<Token> t = tokenizer.tokenize("Hello World");
		
		assertEquals(3, t.size());
		assertEquals("Hello", t.get(0).getText());
		assertEquals(" ", t.get(1).getText());
		assertEquals("World", t.get(2).getText());

	}

	
	@Test
	public void testPunctation() {
		Tokenizer tokenizer = new Tokenizer();
		
		List<Token> t = tokenizer.tokenize("Hello World?");
		
		assertEquals(4, t.size());
		assertEquals("Hello", t.get(0).getText());
		assertEquals(" ", t.get(1).getText());
		assertEquals("World", t.get(2).getText());
		assertEquals("?", t.get(3).getText());

	}
}
