package de.frittenburger.interfaces;

import java.io.File;
import java.io.IOException;

import de.frittenburger.model.DetectMatch;

public interface DetectorService {

	DetectMatch detect(File file) throws IOException;

}
