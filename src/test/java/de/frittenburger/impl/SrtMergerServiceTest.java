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

	@Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Test
	public void test1() throws IOException {
		
		
		SrtMergerService service = new SrtMergerServiceImpl();
		
		File mergeFile = new File("tmp/grantorino/merge.txt"); //tempFolder.newFile("merge.txt");
		
		File[] srtFiles = new File("tmp/grantorino").listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".srt");
			}});
		service.merge(srtFiles, mergeFile);
		
		assertTrue(mergeFile.length() > 10);
	}
	
	@Test
	public void test2() throws IOException {
		
		
		SrtMergerService service = new SrtMergerServiceImpl();
		
		File mergeFile = new File("tmp/elbar/merge.txt"); //tempFolder.newFile("merge.txt");
		
		File[] srtFiles = new File("tmp/elbar").listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".srt");
			}});
		service.merge(srtFiles, mergeFile);
		
		assertTrue(mergeFile.length() > 10);
	}

	@Test
	public void test3() throws IOException {
		
		
		SrtMergerService service = new SrtMergerServiceImpl();
		
		File mergeFile = new File("tmp/spiderman/merge.txt"); //tempFolder.newFile("merge.txt");
		
		File[] srtFiles = new File("tmp/spiderman").listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".srt");
			}});
		service.merge(srtFiles, mergeFile);
		
		assertTrue(mergeFile.length() > 10);
	}
	
	@Test
	public void test4() throws IOException {
		
		
		SrtMergerService service = new SrtMergerServiceImpl();
		
		File mergeFile = new File("tmp/kammerflimmern/merge.txt"); //tempFolder.newFile("merge.txt");
		
		File[] srtFiles = new File("tmp/kammerflimmern").listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".srt");
			}});
		service.merge(srtFiles, mergeFile);
		
		assertTrue(mergeFile.length() > 10);
	}
	
	
}
