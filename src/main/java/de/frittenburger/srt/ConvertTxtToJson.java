package de.frittenburger.srt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ConvertTxtToJson {

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		//convert("data/out/casapapel01_merge_gen.txt","data/out/casapapel01.json");
		convert("data/in/elbar_merge_manual.txt","data/out/elbar.json");
	}	
	
	static void convert(String txtFile,String jsonFile) throws JsonGenerationException, JsonMappingException, IOException {

		
		List<Record> records = new ArrayList<Record>();
		
		String encoding = "UTF8";
		BufferedReader in = null;
		Record rec = null;
		try {

			in = new BufferedReader(new InputStreamReader(new FileInputStream(txtFile), encoding));

			String str;
			// es 333 00:30:10:727 -> 00:30:12:435 [Es una mula.]

			Pattern p = Pattern.compile("([a-z]+) \\d+ (\\d+):(\\d+):(\\d+):(\\d+) -> (\\d+):(\\d+):(\\d+):(\\d+) \\[(.+)\\]");
			while ((str = in.readLine()) != null) {
				
				if(str.trim().equals("[") && rec == null)
				{
					rec = new Record();
					continue;
				}
				if(str.trim().equals("]") && rec != null)
				{
					records.add(rec);
					rec = null;
					continue;
				}
				Matcher m = p.matcher(str);
				if(m.find() && rec != null) {
					
					//"([a-z]+) \\d+ (\\d+):(\\d+):(\\d+),(\\d+) -> (\\d+):(\\d+):(\\d+),(\\d+) [[](.+)[]]"
					String lang = m.group(1);

					
					long mfrom = Integer.parseInt(m.group(2)) * 60 * 60 * 1000
				        + Integer.parseInt(m.group(3)) * 60 * 1000
				        + Integer.parseInt(m.group(4)) * 1000
				        + Integer.parseInt(m.group(5));

					long mto = Integer.parseInt(m.group(6)) * 60 * 60 * 1000
					        + Integer.parseInt(m.group(7)) * 60 * 1000
					        + Integer.parseInt(m.group(8)) * 1000
					        + Integer.parseInt(m.group(9));
					
					String text = m.group(10);
					
					Entry e = new Entry();
					e.setLang(lang);
					e.setFrom(mfrom);
					e.setTo(mto);
					e.setText(text);
					rec.add(e);
					
					continue;
				}
				
				System.out.println(str);
				
			}
		} finally
		{
			if(in != null)
				in.close();
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFile), records);
	}

}
