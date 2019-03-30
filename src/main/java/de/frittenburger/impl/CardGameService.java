package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.interfaces.GameService;
import de.frittenburger.model.card.Card;
import de.frittenburger.model.card.CardSet;
import de.frittenburger.model.card.CardSetList;
import de.frittenburger.model.card.TextPart;
import de.frittenburger.srt.SrtMergeReader;
import de.frittenburger.srt.SrtMergeReaderWrapper;
import de.frittenburger.text.api.TranslateService;
import de.frittenburger.text.api.Translation;
import de.frittenburger.text.api.TranslationPart;


public class CardGameService implements GameService {

	private final Logger logger = LogManager.getLogger(CardGameService.class);

	@Override
	public void generateDataSet(File mergeFile, File datasetJsonFile) throws IOException {

		CardSetList cardsets = new CardSetList();

		SrtMergeReader reader = new SrtMergeReader(mergeFile);
		SrtMergeReaderWrapper wrapper = new SrtMergeReaderWrapper(reader);
		

		
		List<String> languages = wrapper.getLanguages();
		if(languages.size() != 2)
			throw new IOException("Maping with "+languages+" is not posible");
		
		
		String lang0 = languages.get(0);
		String lang1 = languages.get(1);


		TranslateService translateService = TranslateService.getInstance();
		
		for(int i = 0;i < wrapper.size();i++)
		{
			String textstr0 = wrapper.getText(i, languages.get(0));
			if(textstr0 == null) textstr0 = "";
			String textstr1 = wrapper.getText(i, languages.get(1));
			if(textstr1 == null) textstr1 = "";

			
			Translation translation0 = translateService.translate(lang0,lang1,textstr0);
			Translation translation1 = translateService.translate(lang1,lang0,textstr1);
			Card card0 = createCard(translation0);
			Card card1 = createCard(translation1);
			
		    if(card0 == null || card1 == null)
		    {
		    	logger.error(textstr0 +" >> " + card0);
		    	logger.error(textstr1 +" >> " + card1);
		    	continue;
		    }

			CardSet cards = new CardSet();
			cardsets.addCardSetsItem(cards);
			
			cards.putCardsItem(lang0, card0);
			cards.putCardsItem(lang1, card1);

		}
		
		
		
		
		
		
		
		new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(datasetJsonFile, cardsets);
		
	}



	private Card createCard(Translation translation) {
	
		Card card = new Card();
		int i = 0;
		
		List<TranslationPart> parts = translation.getParts();
		
		if(parts == null || parts.isEmpty()) return null;
		
		for(TranslationPart part : parts)
		{
			TextPart textPart = new TextPart();
			card.addTextPartsItem(textPart);
			textPart.setToken(part.getText());
			if(part.hasTranslation())
			{	
				++i;
				card.putAnnotationsItem("anno"+i, part.getTranslation());
				textPart.setAnnotationId("anno"+i);
			}	
			
		}
		return card;
	}



	@Override
	public String getName() {
		return "cards";
	}

	

}
