package de.frittenburger.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.interfaces.GameService;
import de.frittenburger.model.card.Card;
import de.frittenburger.model.card.CardSet;
import de.frittenburger.model.card.CardSetList;


public class CardGameServiceTest {

	@Rule
    public TemporaryFolder folder= new TemporaryFolder();
	
	
	@Test
	public void test() throws IOException {
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		File mergefile = new File(classLoader.getResource("mrec/merge_de_es.txt").getFile());
		File cardsFile = folder.newFile("cards.json");
		GameService service = new CardGameService();
		service.generateDataSet(mergefile, cardsFile);
		
		
		assertTrue(cardsFile.length() > 10);
		CardSetList dataset = new ObjectMapper().readValue(cardsFile, CardSetList.class);
		
		assertNotNull(dataset);
		assertEquals(688,dataset.getCardSets().size());
		
		CardSet cards = dataset.getCardSets().get(0);
		assertEquals(2,cards.getCards().size());
		
		Card card_de = cards.getCards().get("de");
		Card card_es = cards.getCards().get("es");
		assertNotNull(card_de);
		assertNotNull(card_es);

		assertEquals(10,card_de.getTextParts().size());
		

	}

}
