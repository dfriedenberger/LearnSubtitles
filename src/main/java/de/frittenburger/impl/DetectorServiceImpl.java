package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

import de.frittenburger.interfaces.DetectorService;
import de.frittenburger.interfaces.EncodingDetectorService;
import de.frittenburger.interfaces.LanguageDetectorService;
import de.frittenburger.model.DetectMatch;

public class DetectorServiceImpl implements DetectorService {

	private EncodingDetectorService encodingDetectorService = new EncodingDetectorServiceImpl();
	private LanguageDetectorService languageDetectorService = new LanguageDetectorServiceImpl();

	@Override
	public DetectMatch detect(File file) throws IOException {

		DetectMatch m = new DetectMatch();
		
		String fileName = file.getName();
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		}
		
		m.setExtension(extension);
		
		
		byte b[] = Files.readAllBytes(file.toPath());
		
		String encoding = encodingDetectorService.detect(b);
		m.setEncoding(encoding);
		
		String language = languageDetectorService.detect(new String(b,encoding));
		m.setLanguage(encoding);

		
		return m;
	}



}
