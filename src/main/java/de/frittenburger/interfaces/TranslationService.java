package de.frittenburger.interfaces;

import java.io.File;
import java.io.IOException;

public interface TranslationService {

	void translate(File mergeFile, File translationFile) throws IOException;

}
