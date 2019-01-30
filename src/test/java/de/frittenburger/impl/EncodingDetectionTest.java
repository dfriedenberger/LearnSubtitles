package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;

import de.frittenburger.impl.EncodingDetectorServiceImpl;
import de.frittenburger.interfaces.EncodingDetectorService;

public class EncodingDetectionTest {

	@Test
	public void test_utf_8() throws IOException {

	
		EncodingDetectorService service = new EncodingDetectorServiceImpl();
		
		byte[] utf8 = bytesFrom("encoding/utf-8-file.txt");
		
		assertEquals("UTF-8",service.detect(utf8));
	
	}

	
	@Test
	public void test_windows_1251() throws IOException {

	
		EncodingDetectorService service = new EncodingDetectorServiceImpl();
		
		byte[] utf8 = bytesFrom("encoding/windows-1251-file.txt");
		
		assertEquals("WINDOWS-1251",service.detect(utf8));
	
	}

	private byte[] bytesFrom(String name) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(name).getFile());
		return Files.readAllBytes(file.toPath());
	}

}
