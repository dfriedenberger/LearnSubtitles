package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.frittenburger.impl.UnzipServiceImpl;
import de.frittenburger.interfaces.UnzipService;

public class UnzipServiceTest {

	@Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Test
	public void test() throws IOException {
		UnzipService service = new UnzipServiceImpl();
		
		File path = tempFolder.newFolder("testfolder");
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test.zip").getFile());
		
		File nFile = service.extract(file, path, ".txt");
		
		File[] files = path.listFiles();
		assertEquals(1,files.length);
		assertEquals("test.txt",files[0].getName());
		assertEquals(nFile,files[0]);
		


	}

}
