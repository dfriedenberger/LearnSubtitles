package de.frittenburger.srt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class SrtMergerImpl implements SrtMerger {

		
    @Override
	public List<SrtCluster> merge(SrtReader srtreader1, SrtReader srtreader2) throws IOException {

		SrtCluster cluster = new SrtCluster();
				
		int i1 = 0;
		int i2 = 0;

		while(true)
		{
			SrtRecord rec1 = srtreader1.get(i1);
			SrtRecord rec2 = srtreader2.get(i2);
			
			if((rec1 == null) && (rec2 == null))
				break;
			
			
		    if(rec2 == null)
		    {
		    	cluster.add(rec1);
				i1++;
				continue;
		    }

			if(rec1 == null)
			{
				cluster.add(rec2);
				i2++;
				continue;
			}
			
			if(rec2.getFrom() < rec1.getFrom())
			{
				cluster.add(rec2);
				i2++;
				continue;
			}
			
			cluster.add(rec1);
			i1++;
		}
		
		
		List<SrtCluster> clusterList = split(cluster);
		
		
		
		//join
		for(int i = 0;i < clusterList.size();i++)
		{
			SrtCluster cl = clusterList.get(i);
			Map<String,Integer> counter = cl.getCounter();
			
			
			
			if(counter.size() != 2)
			{
				//System.out.println("Error: "+counter);
				String lang = counter.keySet().iterator().next();
				long dist0 = Long.MAX_VALUE;
				long dist2 = Long.MAX_VALUE;
				if(i - 1 > 0)
				{
					SrtCluster cl0 = clusterList.get(i-1);
					dist0 = dist(cl0,cl,lang);
				}
				if(i + 1 < clusterList.size())
				{
					SrtCluster cl2 = clusterList.get(i+1);
					dist2 = dist(cl,cl2,lang);
				}
					
				if(dist0 < dist2 && dist0 < 2500)
				{
					//join with i-1
					clusterList.get(i-1).addAll(cl);
					clusterList.remove(i);
					i--;
				}
				
				if(dist2 < dist0 && dist2 < 2500)
				{
					//join with i+1
					clusterList.get(i+1).addAll(cl);
					clusterList.remove(i);
					i--;
				}
				

			}
			
			
			
		}
		
		
		return clusterList;
		
		
	}

	private long dist(SrtCluster cl0, SrtCluster cl1,String lang) {
		
		SrtRecord rec0 = cl0.getLast(lang);
		SrtRecord rec1 = cl1.getFirst(lang);

		if(rec0 == null || rec1 == null)
			return Long.MAX_VALUE;
		
		return rec1.getFrom() - rec0.getTo();
	}

	private List<SrtCluster> split(SrtCluster cluster) {

	
		//long range = cluster.get(cluster.size() -  1).getTo() - cluster.get(0).getFrom();
		
		if(cluster.size() < 8) //Abbruch bedingung
		{
			List<SrtCluster> list = new ArrayList<SrtCluster>();
			list.add(cluster);
			return list;
		}
		
		long lasttime = cluster.get(0).getFrom();
		long maxdiff = 100;
		int ix = -1;
		
		for(int i = 1 ;i  < cluster.size();i++)
		{
				SrtRecord rec = cluster.get(i);
				long diff = rec.getFrom() - lasttime;
				
				if(diff > maxdiff)
				{
					maxdiff = diff;
					ix = i;
				}
				
				lasttime = rec.getFrom();

				//lasttime = rec.getTo();
		}
	
		if(ix < 0)
		{
			List<SrtCluster> list = new ArrayList<SrtCluster>();
			list.add(cluster);
			return list;
		}
	
		SrtCluster cl1 = new SrtCluster();
		cl1.addAll(cluster.subList(0, ix));
		
		SrtCluster cl2 = new SrtCluster();
		cl2.addAll(cluster.subList(ix,cluster.size()));
				
		
		List<SrtCluster> list = new ArrayList<SrtCluster>();
		list.addAll(split(cl1));
		list.addAll(split(cl2));

		return list;
	}

	


	


	
		
}

	
