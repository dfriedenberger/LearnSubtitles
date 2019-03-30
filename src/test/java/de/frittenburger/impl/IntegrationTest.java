package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.frittenburger.interfaces.SrtMergerService;
import de.frittenburger.interfaces.GameService;
import de.frittenburger.srt.SrtCluster;
import de.frittenburger.srt.SrtMergeReader;
import de.frittenburger.srt.SrtMergerImpl;
import de.frittenburger.srt.SrtMergerImpl2;

public class IntegrationTest {

	private void integrationTest(String movie) throws IOException {

		if(!new File("tmp").exists()) return;
	
		SrtMergerService mergeService = new SrtMergerServiceImpl(new SrtMergerImpl());
		SrtMergerService mergeService2 = new SrtMergerServiceImpl(new SrtMergerImpl2());

		File mergeFile = new File("tmp/"+movie+"/merge.txt"); 
		File mergeFile2 = new File("tmp/"+movie+"/merge2.txt"); 


		File[] srtFiles = new File("tmp/"+movie).listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".srt");
			}});
		
		
		
		
		mergeService.merge(srtFiles, mergeFile);
		mergeService2.merge(srtFiles, mergeFile2);

		List<SrtCluster> cluster = new SrtMergeReader(mergeFile).read();
		
		List<SrtCluster> cluster2 = new SrtMergeReader(mergeFile2).read();

		//Create Cards
		GameService cardGameService = new CardGameService();
		File cardsFile = new File("tmp/"+movie+"/cards.json"); 
		cardGameService.generateDataSet(mergeFile,cardsFile);
		
		//Create Vocabulary
		GameService vocabularyGameService = new VocabularyGameService();
		File vocabularyFile = new File("tmp/"+movie+"/vocabulary.json"); 

		vocabularyGameService.generateDataSet(mergeFile,vocabularyFile);
		
		assertTrue(mergeFile.length() > 10);
		assertTrue(cardsFile.length() > 10);
		assertTrue(vocabularyFile.length() > 10);

	}
	
	@Test
	public void test1() throws IOException {
		integrationTest("grantorino");
	}
	
	

	@Test
	public void test2() throws IOException {
		integrationTest("elbar");
	}

	@Test
	public void test3() throws IOException {
		integrationTest("spiderman");
	}
	
	@Test
	public void test4() throws IOException {
		integrationTest("kammerflimmern");
	}
	
	@Test
	public void test5() throws IOException {
		integrationTest("thepianist");
	}
	
	@Test
	public void test6() throws IOException {
		integrationTest("alien");
	}
	@Test
	public void test7() throws IOException {
		integrationTest("deutschland");
	}
}
