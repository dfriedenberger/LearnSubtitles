package de.frittenburger;

import static org.junit.Assert.*;

import org.junit.Test;

import de.frittenburger.impl.EncodingDetectorServiceImpl;
import de.frittenburger.impl.LanguageDetectorServiceImpl;
import de.frittenburger.interfaces.EncodingDetectorService;
import de.frittenburger.interfaces.LanguageDetectorService;

public class LanguageDetectionTest {

	@Test
	public void test_EN() {
		LanguageDetectorService service = new LanguageDetectorServiceImpl();
		
		assertEquals("en",service.detect("In the example below, the text is about ..."));

		
	}
	@Test
	public void test_ES() {
		LanguageDetectorService service = new LanguageDetectorServiceImpl();
		
		assertEquals("es",service.detect("Hola mundo"));

		
	}
	
	@Test
	public void test_DE() {
		LanguageDetectorService service = new LanguageDetectorServiceImpl();
		
		assertEquals("de",service.detect("Hallo Welt"));

		
	}
}
