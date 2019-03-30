package de.frittenburger.srt;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.frittenburger.srt.SrtCluster;

public class SrtMergerTest {

	@Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Test
	public void test() throws IOException {
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file1 = new File(classLoader.getResource("srt/part.es.utf8.srt").getFile());
		File file2 = new File(classLoader.getResource("srt/part.de.utf8.srt").getFile());

		
		SrtReader srtReader1 = new SrtReader();
		srtReader1.load("es",file1.getPath(),new DefaultFilter(),"UTF8");
		
		SrtReader srtReader2 = new SrtReader();
		srtReader2.load("de",file2.getPath(),new DefaultFilter(),"UTF8");
		
		SrtMergerImpl2 merger = new SrtMergerImpl2();
		List<SrtCluster> clusterList = merger.merge(srtReader1,srtReader2);
		
		
		
		for(SrtCluster cl : clusterList)
		{
			Map<String, Integer> l = cl.getCounter();
			System.out.println(l);
			System.out.println("Size "+cl.size());
		
			for(int i = 0;i < cl.size();i++)
				System.out.println(cl.get(i));

		}

		//TODO assertEquals(0, error);
	}

	@Test
	public void testCompress() throws IOException {
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file1 = new File(classLoader.getResource("srt/part.es.utf8.srt").getFile());
		File file2 = new File(classLoader.getResource("srt/part.de.utf8.srt").getFile());

		
		SrtReader srtReader1 = new SrtReader();
		srtReader1.load("es",file1.getPath(),new DefaultFilter(),"UTF8");
		
		SrtReader srtReader2 = new SrtReader();
		srtReader2.load("de",file2.getPath(),new DefaultFilter(),"UTF8");
		

		
		SrtMergerImpl2 merger = new SrtMergerImpl2();
		
		List<SrtCluster> clusterList1 = merger.read(srtReader1);
		List<SrtCluster> clusterList2 = merger.read(srtReader2);

		merger.compress(clusterList1,2500); //Spanisch
		merger.compress(clusterList2,2500);

		
		for(SrtCluster cl : clusterList1)
		{
			System.out.println("Size "+cl.size());
			for(int i = 0;i < cl.size();i++)
				System.out.println(cl.get(i));

		}
		System.out.println("----------------------------");

		for(SrtCluster cl : clusterList2)
		{
			System.out.println("Size "+cl.size());
			for(int i = 0;i < cl.size();i++)
				System.out.println(cl.get(i));

		}
		assertEquals(2 + 1/* Werbung */, clusterList1.size());
		assertEquals(2, clusterList2.size());

	}
	
	
	
	@Test
	public void testCluster() throws IOException {
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file1 = new File(classLoader.getResource("srt/subpart.es.utf8.srt").getFile());
		File file2 = new File(classLoader.getResource("srt/subpart.de.utf8.srt").getFile());

		
		SrtReader srtReader1 = new SrtReader();
		srtReader1.load("es",file1.getPath(),new DefaultFilter(),"UTF8");
		
		SrtReader srtReader2 = new SrtReader();
		srtReader2.load("de",file2.getPath(),new DefaultFilter(),"UTF8");
		

		
		SrtMergerImpl2 merger = new SrtMergerImpl2();
		
		List<SrtCluster> clusterList1 = merger.read(srtReader1);
		List<SrtCluster> clusterList2 = merger.read(srtReader2);

		merger.cluster(clusterList1); 
		merger.cluster(clusterList2);

		
		for(SrtCluster cl : clusterList1)
		{
			long from = cl.getFirst().getFrom();
			long to = cl.getLast().getTo();
			System.out.println("size="+cl.size()+" duration="+(to - from));
			for(int i = 0;i < cl.size();i++)
				System.out.println(cl.get(i));

		}
		System.out.println("----------------------------");

		for(SrtCluster cl : clusterList2)
		{
			long from = cl.getFirst().getFrom();
			long to = cl.getLast().getTo();
			System.out.println("size="+cl.size()+" duration="+(to - from));
			for(int i = 0;i < cl.size();i++)
				System.out.println(cl.get(i));

		}
		//TODO assertEquals(clusterList1.size(),clusterList2.size());

	}
	
	

	
	@Test
	public void testDumpDifference() throws IOException {
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		
		File file1 = new File(classLoader.getResource("srt/part.es.utf8.srt").getFile());
		File file2 = new File(classLoader.getResource("srt/part.de.utf8.srt").getFile());

		
		SrtReader srtReader1 = new SrtReader();
		srtReader1.load("es",file1.getPath(),new DefaultFilter(),"UTF8");
		
		SrtReader srtReader2 = new SrtReader();
		srtReader2.load("de",file2.getPath(),new DefaultFilter(),"UTF8");
		
		int i = 0;
		while(true)
		{
			SrtRecord rec1 = srtReader1.get(i);
			if(rec1 == null) break;
			if(i > 0)
			{
				SrtRecord rec0 = srtReader1.get(i-1);
				System.out.println(rec1.getFrom() - rec0.getTo());
			}
			System.out.println(rec1.getText());
			
		    i++;
		}
		
		System.out.println("----------------------------");

		 i = 0;
		while(true)
		{
			SrtRecord rec1 = srtReader2.get(i);
			if(rec1 == null) break;
			if(i > 0)
			{
				SrtRecord rec0 = srtReader2.get(i-1);
				System.out.println(rec1.getFrom() - rec0.getTo());
			}
			System.out.println(rec1.getText());
			
		    i++;
		}
	}
	
}
