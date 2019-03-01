package de.frittenburger.srt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.frittenburger.impl.Tokenizer;
import de.frittenburger.model.Token;
import de.frittenburger.model.TokenList;

public class SrtMerger2  {

	public List<SrtCluster> merge(SrtReader srtReader1, SrtReader srtReader2) {

	
		List<SrtCluster> cl1 = read(srtReader1);
		List<SrtCluster> cl2 = read(srtReader2);

		cluster(cl1);
		cluster(cl2);

		
		
		return merge(cl1,cl2);
	}

	public List<SrtCluster> merge(List<SrtCluster> in1, List<SrtCluster> in2) {

		List<SrtCluster> out = new ArrayList<>();
		int ix1 = 0;
		int ix2 = 0;
		while(true)
		{
			SrtCluster cl1 = null;
			SrtCluster cl2 = null;
			if(ix1 < in1.size())
				cl1 = in1.get(ix1);
			
			if(ix2 < in2.size())
				cl2 = in2.get(ix2);
			
			
			if(cl1 == null && cl2 == null) break;
			if(cl1 == null && cl2 != null) 
			{
				out.add(cl2);
				ix2++;
				continue;
			}
			if(cl1 != null && cl2 == null) 
			{
				out.add(cl1);	
				ix1++;
				continue;
			}
			
			long x1 = cl1.getFirst().getFrom();
			long y1 = cl1.getLast().getTo();
			long x2 = cl2.getFirst().getFrom();
			long y2 = cl2.getLast().getTo();
		
			if(y1 < x2)
			{
				out.add(cl1);	
				ix1++;
				continue;
			}
			
			if(y2 < x1)
			{
				out.add(cl2);
				ix2++;
				continue;
			}
			
			//Join
			cl1.addAll(cl2);
			
			
			List<SrtRecord> cl = cl1.stream().sorted((p1, p2) -> Long.compare(p1.getFrom(),p2.getFrom())).collect(Collectors.toList());
			SrtCluster ncl = new SrtCluster();
			ncl.addAll(cl);
			out.add(ncl);
			ix1++;
			ix2++;
		}
		
		
		
		return out;
	}

	public void cluster(List<SrtCluster> in) {
		
		
		while(true)
		{
			int ix = -1;
			long mindist = 0;
			for(int i = 1; i < in.size();i++)
			{
				SrtCluster cl0 = in.get(i-1);
				SrtCluster cl1 = in.get(i);
				
				if(cl0.size() + cl1.size() > 3) continue;
				
				long dist = cl1.getFirst().getFrom() - cl0.getLast().getTo();
				if(ix != -1 && (dist > mindist)) continue;
				if(dist > 5000) continue;
				mindist = dist;
				ix = i;
			}
			
			if(ix == -1) break;
			
			//join
			in.get(ix -1).addAll(in.remove(ix));
		
			
		}
		
	}
	public void compress(List<SrtCluster> in,long maxdiff) {
		
			int i = 1;
			while(i < in.size())
			{
				SrtCluster cl0 = in.get(i-1);
				SrtCluster cl1 = in.get(i);
			
				long dist = cl1.getFirst().getFrom() - cl0.getLast().getTo();
				if(dist <= maxdiff)
				{
					//join
					in.get(i -1).addAll(in.remove(i));
					continue;	
				}
				i++;
			}

	}

	public List<SrtCluster> read(SrtReader srtReader) {
		
		List<SrtCluster> list = new ArrayList<>();
		int i = 0;
		while(true)
		{
			SrtRecord rec = srtReader.get(i++);
			if(rec == null)
				break;
			
			SrtCluster cl = new SrtCluster();
			cl.add(rec);
			list.add(cl);
		}
		return list;
	}

	public boolean match(SrtRecord rec1, SrtRecord rec2) {

		
		Tokenizer tokenizer = new Tokenizer();
		
		
		Set<String> words = new HashSet<String>();
		int match = 0;
		
		for(String text1 : rec1.getText())
		{
			TokenList tl1 = tokenizer.tokenize(text1);
			for(Token t : tl1)
			{
				if(t.getType() != 0) continue;
				words.add(t.getText());
			}
		}
		
		Set<String> words2 = new HashSet<String>();
		for(String text2 : rec2.getText())
		{
			TokenList tl2 = tokenizer.tokenize(text2);
			for(Token t : tl2)
			{
				if(t.getType() != 0) continue;
				words2.add(t.getText());
				if(words.contains(t.getText()))
					match++;
			}
		}
		//System.out.println(words + " == "+words2);
		return match > 0;
	}

}
