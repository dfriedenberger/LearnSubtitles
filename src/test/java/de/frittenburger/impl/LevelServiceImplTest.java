package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import de.frittenburger.interfaces.LevelService;

public class LevelServiceImplTest {

	@Test
	public void test() throws IOException {
		
		ClassLoader classLoader1 = LevelServiceImpl.class.getClassLoader();
		File fileLevel = new File(classLoader1.getResource("dict/es/frequency.txt").getFile());
		
		LevelService service = new LevelServiceImpl(fileLevel);
		
		assertEquals(0,service.testLevel("de"));
		assertEquals(1,service.testLevel("nada"));
		assertEquals(2,service.testLevel("puedo"));
		assertEquals(3,service.testLevel("mucho"));
		assertEquals(9,service.testLevel("venir"));

	}

}
