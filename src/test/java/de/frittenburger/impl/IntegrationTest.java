package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.frittenburger.interfaces.SrtMergerService;
import de.frittenburger.interfaces.TranslationService;

public class IntegrationTest {

	private void integrationTest(String movie) throws IOException {

		if(!new File("tmp").exists()) return;
	
		SrtMergerService mergeService = new SrtMergerServiceImpl();
		TranslationService translationService = new TranslationServiceImpl();

		File mergeFile = new File("tmp/"+movie+"/merge.txt"); 
		File translationFile = new File("tmp/"+movie+"/translate.json"); 

		File[] srtFiles = new File("tmp/"+movie).listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".srt");
			}});
		
		mergeService.merge(srtFiles, mergeFile);
		translationService.translate(mergeFile,translationFile);
		assertTrue(mergeFile.length() > 10);
		assertTrue(translationFile.length() > 10);

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
