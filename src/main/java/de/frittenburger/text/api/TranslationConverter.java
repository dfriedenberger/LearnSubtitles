package de.frittenburger.text.api;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TranslationConverter {

	public Translation convert(String data) throws IOException {
		JsonNode root = new ObjectMapper().readTree(data);
		
		Translation translation = new Translation();
		
		JsonNode groupsArray = root.get("groups");
		JsonNode transArray = root.get("translation");

		
		for(int i = 0;i < groupsArray.size();i++)
		{
			TranslationPart part = new TranslationPart();
			translation.addPartsItem(part);
			JsonNode group = groupsArray.get(i);
			
			for(JsonNode token : group.get("tokens"))
			{
				String t = token.get("text").asText();
				part.setTextItem(t);
			}
			
			JsonNode trans = transArray.get(i);
			if(!trans.asText().equals("null"))
			{
				part.setTranslation(trans.get("candidates").get(0).asText());
			}
			
			
			
		}
		
		
		return translation;
	}

}
