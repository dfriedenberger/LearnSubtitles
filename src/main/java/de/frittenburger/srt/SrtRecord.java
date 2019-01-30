package de.frittenburger.srt;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SrtRecord {



	private int nr;
	private long from;
	private long to;
	private List<String> textes = new ArrayList<String>();
	private String lang;

	public SrtRecord(String lang,int nr) {
		this.lang = lang;
		this.nr = nr;
	}

	public void setTime(long from, long to) {
		this.from = from;
		this.to = to;
	}

	public void addText(String text) {
		textes.add(text);
	}

	public long getFrom() {
		return from;
	}
	
	public long getTo() {
		return to;
	}
	
	@Override
	public String toString() {
		return lang+" "+nr + " " + timestr(from) + " -> " + timestr(to) + " [" + join(textes," ")+"]";
	}

	private String join(List<String> textList, String space) {
        String line = "";
		for(int i = 0;i < textList.size();i++)
		{
			if(i > 0) line += " ";
			line += textList.get(i);			
		}
		
		return line;
	}

	static String timestr(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		return sdf.format(new Date(time - 60 * 60 * 1000));
	}

	public String getLang() {
		return lang;
	}

	public List<String> getText() {
		return textes;
	}

	
}
