package de.frittenburger.interfaces;

import java.util.List;

import de.frittenburger.model.Annotation;
import de.frittenburger.model.TokenList;

public interface LanguageProcessingService {

	List<Annotation> process(TokenList text);

}
