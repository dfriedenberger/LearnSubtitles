package de.frittenburger.text.api;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;


public class TranslateServiceTest {

	@Test
	public void testDe() throws IOException {
		
		String textstr = "Nein ! Sei nicht so hysterisch Marta ! Hör auf , bitte . Setz dich doch in unsere Nähe und sag mir über WhatsApp , ob er dir gefällt .";
		
		TranslateService translateService = TranslateService.getInstance();
		Translation translation = translateService.translate("de", "es", textstr);
		assertNotNull(translation);
		assertNotNull(translation.getParts());
		assertEquals(13,translation.getParts().stream().filter(tp -> tp.getText() != null).count());
		assertEquals(2,translation.getParts().stream().filter(tp -> tp.getTranslation() != null).count());

		String data = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(translation);
		System.out.println(data);
	}

	
	
	
}
