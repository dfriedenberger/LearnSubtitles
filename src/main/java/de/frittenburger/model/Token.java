package de.frittenburger.model;

public class Token {

	public static final int WORD = 0;
	public static final int SPACE = 1;
	public static final int SPECIAL = 2;
	private final int type;
	private final StringBuffer text = new StringBuffer();

	public Token(int type) {
		this.type = type;
	}

	public Token(String text) {
		this.type = WORD;
		this.text.append(text);
	}

	public String getText() {
		return text.toString();
	}
	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Token [type=" + type + ", text=" + text + "]";
	}

	public void add(char c) {
		text.append(c);
	}

	public String getKey() {
		return text.toString().toLowerCase();
	}


	
}
