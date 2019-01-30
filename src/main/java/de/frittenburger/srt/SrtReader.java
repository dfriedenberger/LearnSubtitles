package de.frittenburger.srt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SrtReader {

	private static final String UTF8_BOM = "\uFEFF";
	private List<SrtRecord> records = new ArrayList<SrtRecord>();

	public void load(String lang,String filename, Filter filter, String encoding) throws IOException {
		
		
		BufferedReader in = null;
		try {

			in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), encoding));

			String str;
			SrtRecord rec = null;
			Pattern numberLine = Pattern.compile("^(\\d+)$");

			Pattern timeLine = Pattern.compile("(\\d+):(\\d+):(\\d+),(\\d+) --> (\\d+):(\\d+):(\\d+),(\\d+)");
			int line = 0;
			int recordNumber = -1;
			while ((str = in.readLine()) != null) {
				
				line++;

				//BOM am Anfang entfernen
				if(line == 1 && str.startsWith(UTF8_BOM))
				{
					str = str.substring(1);
				}
				
				Matcher mNumber = numberLine.matcher(str.trim());
				if(mNumber.find())
				{
					//it's a Number first Line?
					recordNumber = Integer.parseInt(mNumber.group(1));
					continue;
				}
				
				Matcher mTime = timeLine.matcher(str);
				if(mTime.find())
				{
					rec = new SrtRecord(lang,recordNumber);

					//set time
					long msec = Integer.parseInt(mTime.group(1)) * 60 * 60 * 1000
				        + Integer.parseInt(mTime.group(2)) * 60 * 1000
				        + Integer.parseInt(mTime.group(3)) * 1000
				        + Integer.parseInt(mTime.group(4));

					long mto = Integer.parseInt(mTime.group(5)) * 60 * 60 * 1000
					        + Integer.parseInt(mTime.group(6)) * 60 * 1000
					        + Integer.parseInt(mTime.group(7)) * 1000
					        + Integer.parseInt(mTime.group(8));
					rec.setTime(msec,mto);
					continue;
				}
				
				//filter 
				str = filter.filter(str);
				
				if(str.trim().isEmpty())
				{
					//ignore
					continue;
				}
				
				if(rec == null)
					throw new RuntimeException("Unknown process line="+line);
				
				if(!records.contains(rec))
					records.add(rec);

				rec.addText(str.trim());
			}

		} finally {
			if (in != null)
				in.close();
		}
	}

	public SrtRecord get(int ix) {
		if(ix < records.size())
			return records.get(ix);
		return null;
	}

	


}
