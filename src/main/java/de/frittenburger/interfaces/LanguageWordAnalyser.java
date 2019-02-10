package de.frittenburger.interfaces;

import de.frittenburger.model.Annotation;
import de.frittenburger.model.TokenList;

public interface LanguageWordAnalyser {

	Annotation analyse(TokenList tokens, int ix);

}
