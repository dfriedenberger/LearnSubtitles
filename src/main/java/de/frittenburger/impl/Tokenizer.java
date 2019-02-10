package de.frittenburger.impl;


import de.frittenburger.model.Token;
import de.frittenburger.model.TokenList;

public class Tokenizer {

	public TokenList tokenize(String text) {

		TokenList list = new TokenList();
		char c[] = text.toCharArray();
		Token token = new Token(-1);
		for(int i = 0;i < c.length;i++)
		{
			int t = type(c[i]);
			if(token.getType() != t)
			{
				token = new Token(t);
				list.add(token);
			}
			token.add(c[i]);
			
		}
			
		return list;
	}

	private int type(char c) {
		if(Character.isLetterOrDigit(c))
			return Token.WORD;
		if(Character.isSpaceChar(c))
			return Token.SPACE;

		return Token.SPECIAL;
		
	}

}
