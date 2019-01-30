package de.frittenburger.srt;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class SrtReaderTest {

	@Test
	public void test() throws IOException {
		
		SrtReader reader = new SrtReader();
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("srt/es.utf8.srt").getFile());
		
		reader.load("es", file.getPath(), new DefaultFilter(), "UTF-8");
		
		int ix = 0;
		
		while(true)
		{
			SrtRecord r = reader.get(ix);
			if(r == null) break;
			System.out.println(r);
			ix++;
		}
		
		
		
		
	}

}
