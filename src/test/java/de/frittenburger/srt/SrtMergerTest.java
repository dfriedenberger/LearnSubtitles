package de.frittenburger.srt;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.frittenburger.impl.SrtMergerServiceImpl;
import de.frittenburger.interfaces.SrtMergerService;
import de.frittenburger.interfaces.TranslationService;
import de.frittenburger.srt.SrtCluster;
import de.frittenburger.srt.SrtMergeReader;
import de.frittenburger.srt.SrtMergeReaderWrapper;
import de.frittenburger.srt.SrtMerger;

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
		
		SrtMerger2 merger = new SrtMerger2();
		List<SrtCluster> clusterList = merger.merge(srtReader1,srtReader2);
		
		
		
		int error = 0;
		for(SrtCluster cl : clusterList)
		{
			Map<String, Integer> l = cl.getCounter();
			System.out.println(l);
			if(l.size() != 2)
				error++;
			
			System.out.println("Size "+cl.size());
			
			if(cl.size() >= 16)
				error++;
		
			
			for(int i = 0;i < cl.size();i++)
				System.out.println(cl.get(i));

		}

		assertEquals(0, error);
	}

	@Test
	public void testCluster() throws IOException {
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file1 = new File(classLoader.getResource("srt/part.es.utf8.srt").getFile());
		File file2 = new File(classLoader.getResource("srt/part.de.utf8.srt").getFile());

		
		SrtReader srtReader1 = new SrtReader();
		srtReader1.load("es",file1.getPath(),new DefaultFilter(),"UTF8");
		
		SrtReader srtReader2 = new SrtReader();
		srtReader2.load("de",file2.getPath(),new DefaultFilter(),"UTF8");
		

		
		SrtMerger2 merger = new SrtMerger2();
		
		List<SrtCluster> clusterList1 = merger.read(srtReader1);
		List<SrtCluster> clusterList2 = merger.read(srtReader2);

		merger.cluster(clusterList1);
		merger.cluster(clusterList2);

		
		for(SrtCluster cl : clusterList1)
		{
			System.out.println("Size "+cl.size());
			for(int i = 0;i < cl.size();i++)
				System.out.println(cl.get(i));

		}
		for(SrtCluster cl : clusterList2)
		{
			System.out.println("Size "+cl.size());
			for(int i = 0;i < cl.size();i++)
				System.out.println(cl.get(i));

		}
		assertEquals(clusterList1.size(), clusterList2.size());
	}
	
	
}
