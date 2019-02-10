package de.frittenburger.impl;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import de.frittenburger.model.TokenList;
import de.frittenburger.model.Translation;

public class DingReader implements Closeable {

	private final BufferedReader in;
	private final String name;
	private int lineNr = 0;

	public DingReader(File file) throws IOException {

			this.name = file.getName();
			this.in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	public Translation next() throws IOException {

		final Tokenizer tokenizer = new Tokenizer();

		while(true)
		{
			
			String line = in.readLine();
			lineNr++;
			if(line == null) break;
			if(line.trim().startsWith("#")) continue;
			if(line.trim().equals("")) continue;

			try
			{
				String[] p =  line.split("::");
				
				if(p.length != 2)
					throw new ParseException(line,0);
			
				TokenList candidate = tokenizer.tokenize(p[0].trim());
				String[] translations = p[1].split(";");		

				Translation record = new Translation(candidate);
				for(String translation : translations)
				{
					record.getAnnotations().add(translation.trim());
				}
				
				return record;

			
			} 
			catch(ParseException e)
			{
				System.err.println(name+"["+lineNr+"] :"+line+" ("+e.getMessage()+")");
			}
			
		}
		
		
		return null;
	}

	
	
	

}
