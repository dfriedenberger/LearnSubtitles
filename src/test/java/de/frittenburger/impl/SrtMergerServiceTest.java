package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.frittenburger.interfaces.SrtMergerService;

public class SrtMergerServiceTest {

	@Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Test
	public void test1() throws IOException {
		
		
		SrtMergerService service = new SrtMergerServiceImpl();
		
		File mergeFile = new File("tmp/grantorino_merge.txt"); //tempFolder.newFile("merge.txt");
		
		File[] srtFiles = new File("tmp/grantorino").listFiles();
		service.merge(srtFiles, mergeFile);
		
		assertTrue(mergeFile.length() > 10);
	}
	
	@Test
	public void test2() throws IOException {
		
		
		SrtMergerService service = new SrtMergerServiceImpl();
		
		File mergeFile = new File("tmp/elbar_merge.txt"); //tempFolder.newFile("merge.txt");
		
		File[] srtFiles = new File("tmp/elbar").listFiles();
		service.merge(srtFiles, mergeFile);
		
		assertTrue(mergeFile.length() > 10);
	}

}
