package de.frittenburger.srt;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;







import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import de.frittenburger.controller.PageController;


public class SrtMergeReader {

	private final Logger logger = LogManager.getLogger(PageController.class);

	private final InputStream is;


	public SrtMergeReader(String txtFile) throws IOException
	{
		this.is = new FileInputStream(txtFile);
	}
	
	
	public SrtMergeReader(byte[] data) {
		this.is = new ByteArrayInputStream(data);
	}


	public List<SrtCluster> read() throws IOException {

		
		List<SrtCluster> records = new ArrayList<SrtCluster>();
		
		String encoding = "UTF8";
		BufferedReader in = null;
		SrtCluster rec = null;
		try {

			in = new BufferedReader(new InputStreamReader(is, encoding));

			String str;
			// es 333 00:30:10:727 -> 00:30:12:435 [Es una mula.]

			Pattern p = Pattern.compile("([a-z]+) (\\d+) (\\d+):(\\d+):(\\d+):(\\d+) -> (\\d+):(\\d+):(\\d+):(\\d+) \\[(.+)\\]");
			while ((str = in.readLine()) != null) {
				
				if(str.trim().equals("[") && rec == null)
				{
					rec = new SrtCluster();
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

					int nr = Integer.parseInt(m.group(2));
					long mfrom = Integer.parseInt(m.group(3)) * 60 * 60 * 1000
				        + Integer.parseInt(m.group(4)) * 60 * 1000
				        + Integer.parseInt(m.group(5)) * 1000
				        + Integer.parseInt(m.group(6));

					long mto = Integer.parseInt(m.group(7)) * 60 * 60 * 1000
					        + Integer.parseInt(m.group(8)) * 60 * 1000
					        + Integer.parseInt(m.group(9)) * 1000
					        + Integer.parseInt(m.group(10));
					
					String text = m.group(11);
					
					SrtRecord e = new SrtRecord(lang,nr);
					e.setTime(mfrom,mto);
					e.addText(text);
					rec.add(e);
					
					continue;
				}
				
				logger.error(str);
				
			}
		} finally
		{
			if(in != null)
				in.close();
		}
		
		
		return records;
	}

}
