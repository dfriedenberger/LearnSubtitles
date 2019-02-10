package de.frittenburger.srt;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SrtMergeReaderWrapper {

	private final Set<String> languages = new HashSet<String>();
	private final List<SrtCluster> clusters;

	public SrtMergeReaderWrapper(SrtMergeReader reader) throws IOException {
		this.clusters = reader.read();
	
		for(int i = 0;i < clusters.size() && languages.size() < 2;i++)
		{
			languages.addAll(clusters.get(i).getCounter().keySet());
		}
		

	}

	public int size() {
		return clusters.size();
	}

	public List<String> getLanguages() {
		return languages.stream().sorted().collect(Collectors.toList());
	}

	public String getText(int i, String lang) {

		SrtCluster cluster = clusters.get(i);
		
		String text = null;
		
		for(SrtRecord r : cluster)
		{
			if(!r.getLang().equals(lang)) continue;
			for(String txt : r.getText())
			{
				if(text == null) 
					text = txt;
				else text += " " + txt;
				
			}
		}
		
		
		return text;
	}
	
	

}
