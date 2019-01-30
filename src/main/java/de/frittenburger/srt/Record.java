package de.frittenburger.srt;

import java.util.ArrayList;
import java.util.List;

public class Record {

	private List<Entry> entries = new ArrayList<Entry>();
	
	
	public void add(Entry e) {
		entries.add(e);
	}

	public List<Entry> getEntries() {
		return entries;
	}


	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	public int size() {
		return this.entries.size();
	}


	

}
