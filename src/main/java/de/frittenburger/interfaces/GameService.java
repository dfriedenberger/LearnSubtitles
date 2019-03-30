package de.frittenburger.interfaces;

import java.io.File;
import java.io.IOException;

public interface GameService {

	String getName();
	void generateDataSet(File mergeFile, File datasetJsonFile) throws IOException;

}
