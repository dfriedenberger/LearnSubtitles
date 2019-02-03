package de.frittenburger.srt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SrtCluster extends ArrayList<SrtRecord> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SrtRecord getLast(String lang) {
		
		for(int i = size() -1;i > 0;i--)
		{
			SrtRecord rec = get(i);
			if(rec.getLang().equals(lang))
				return rec;
		}
		
		return null;
	}

	public SrtRecord getFirst(String lang) {
		
		
		for(int i = 0;i < size();i++)
		{
			SrtRecord rec = get(i);
			if(rec.getLang().equals(lang))
				return rec;
		}
		
		return null;
	}
	
	public Map<String, Integer> getCounter() {
		
		Map<String,Integer> counter = new HashMap<String,Integer>();

		for(SrtRecord r : this)
		{
			String lang = r.getLang();
			int c = 0;
			if(counter.containsKey(lang))
				c = counter.get(lang);
			c++;
			counter.put(lang,c);
		}
		return counter;
		
	}

	

}
