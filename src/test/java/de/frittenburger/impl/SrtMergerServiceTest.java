package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.frittenburger.interfaces.SrtMergerService;

public class SrtMergerServiceTest {

	private void integrationTest(String movie) throws IOException {

		if(!new File("tmp").exists()) return;
	
		SrtMergerService service = new SrtMergerServiceImpl();
		
		File mergeFile = new File("tmp/"+movie+"/merge.txt"); 
		
		File[] srtFiles = new File("tmp/"+movie).listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".srt");
			}});
		service.merge(srtFiles, mergeFile);
		
		assertTrue(mergeFile.length() > 10);
		
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
	
	
}
