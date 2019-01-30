package de.frittenburger.srt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



public class SrtMerger {

	
	
	public void merge(SrtReader srtreaderEs, SrtReader srtreaderDe,File file) throws IOException {

		int ixEs = 0;
		int ixDe = 0;
		
		int error1 = 0;
		int error3 = 0;
		int errore = 0;
		int errorn = 0;
		int matching = 0;
		int todos = 0;

		
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
			    new FileOutputStream(file), "UTF-8")));
		
		List<SrtRecord> rest = new ArrayList<SrtRecord>();
		int pause = 0;
		for(long time = 0;true;time+= 2000)
		{
			List<SrtRecord> list = new ArrayList<SrtRecord>();
			SrtRecord es = null;
			SrtRecord de = null;

			int find = -1;
			while(find != 0)
			{
				find = 0;
				es = srtreaderEs.get(ixEs);
				if(es != null && es.getFrom() <= time)
				{
					list.add(es);
					ixEs++;
					find++;
				}
				
				de = srtreaderDe.get(ixDe);
				if(de != null && de.getFrom() <= time)
				{
					//String text = join(" ", de.getText());
					list.add(de);
					ixDe++;
					find++;
				}
			}
			
		

			if(es == null && de == null) break;
			
			//if match
			if(list.size() == 0)
			{
				pause++;
				if(pause < 3) continue;
				if(rest.size() == 0) continue;
			}
			else
			{
			    pause = 0;
				rest.addAll(list);
				list.clear();
				continue;
			}
				
			switch (rest.size()) {
			case 0:
				break;
			case 1:
				error1++;
				break;
			case 2: // perfekt
				matching++;
				break;
			case 3:
				error3++;
				break;
			case 4:
			case 6:
			case 8:
				errore++;
				break;
			default:
				errorn++;
				break;

			}

			
			
		
			
			List<List<SrtRecord>> grps = split(rest);
		
			for(List<SrtRecord> l : grps)
			{
				int dec = 0;
				int esc = 0;
				for (SrtRecord r1 : l) {
					if(r1.getLang().equals("de"))
						dec++;
					else 
						esc++;
				}
				
				
				if (dec == 0 || esc == 0)
				{
					todos++;
					out.println("#error");
				}
				
				if(l.size() > 16) {
					todos++;
					out.println("#check" + l.size());
				}
				out.println("[ ");
				for (SrtRecord r1 : l) {
					out.println(" " + r1);
				}
				out.println("]");
				
			}
			rest.clear();
			
	
			
			

			
			

		}
		
		out.close();
		System.out.println("error1 "+error1);
		System.out.println("error3 "+error3);
		System.out.println("errore "+errore);
		System.out.println("errorn "+errorn);
		System.out.println("matching "+matching);
		System.out.println("todos "+todos);

		
		
		
	}

	


	private static List<List<SrtRecord>> split(List<SrtRecord> list) {
		
		List<List<SrtRecord>> grps = new ArrayList<List<SrtRecord>>();

		//Abbruchbedingung cannot split
		if(list.size() > 4)
		{
		
			long maxTime = -1;
			int ix = -1;
			for(int i = 1;i < list.size();i++)
			{
				
				long diff = list.get(i).getFrom() - list.get(i - 1).getTo();
				
				if(list.size() < 10 && diff < 500) continue;
				if(diff < 200) continue;

				if(maxTime >= diff) continue;
				maxTime = diff;
				ix = i;
			}
			
			//Abbruchbedingung cannot split
			if(ix > 1)
			{
				List<SrtRecord> l1 = sublist(list,0,ix-1);
				List<SrtRecord> l2 = sublist(list,ix,list.size()-1);
				grps.addAll(split(l1));
				grps.addAll(split(l2));
				return grps;
			}
		
		}
		grps.add(list);
		return grps;
		
		
	
		
		
	}


	private static List<SrtRecord> sublist(List<SrtRecord> list, int f, int l) {
		List<SrtRecord> s = new ArrayList<SrtRecord>();
		for(int i = f;i <= l;i++)
			s.add(list.get(i));
		return s;
	
	
	}




	
		
}

	
