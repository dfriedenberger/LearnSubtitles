package de.frittenburger.interfaces;

import java.util.List;

import de.frittenburger.model.TokenList;

public interface BaseWordService {

	TokenList find(TokenList tokens, int ix,List<Integer> indicies);

}
