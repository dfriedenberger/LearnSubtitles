package de.frittenburger.interfaces;

import java.io.File;
import java.io.IOException;


public interface SrtMergerService {

	void merge(File[] srtFiles, File mergeFile) throws IOException;
	
	
}
