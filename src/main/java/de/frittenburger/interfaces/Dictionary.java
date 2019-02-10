package de.frittenburger.interfaces;

import de.frittenburger.model.Annotation;
import de.frittenburger.model.TokenList;

public interface Dictionary {

	Annotation search(TokenList tokens, int ix);

}
