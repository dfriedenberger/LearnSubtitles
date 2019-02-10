package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.frittenburger.interfaces.LevelService;

public class LevelServiceImpl implements LevelService {

	private List<Set<String>> levels = new ArrayList<>();

	public LevelServiceImpl(File fileLevel) throws IOException {
		for(String line : Files.readAllLines(fileLevel.toPath(),StandardCharsets.UTF_8))
		{
			if(line.trim().startsWith("#")) continue;
			Set<String> words = new HashSet<String>();
			
			
			for(String word :line.split(","))
			{
				String w = word.trim();
				if(w.isEmpty()) continue;
				words.add(w);
			}
			
			if(words.size() == 0) continue;
			levels.add(words);
			
		}	
	}

	@Override
	public int testLevel(String word) {

		int i = 0;
		while(i < levels.size())
		{
			if(levels.get(i).contains(word.toLowerCase())) break;
			i++;
		}
		
		return i;
	}

}
