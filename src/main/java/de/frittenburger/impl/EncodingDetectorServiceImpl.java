package de.frittenburger.impl;

import org.mozilla.universalchardet.UniversalDetector;

import de.frittenburger.interfaces.EncodingDetectorService;

public class EncodingDetectorServiceImpl implements EncodingDetectorService {

	@Override
	public String detect(byte[] b) {
		
		// (1)
		UniversalDetector detector = new UniversalDetector(null);

		// (2)
		detector.handleData(b,0,b.length);
		
		// (3)
		detector.dataEnd();

		// (4)
		String encoding = detector.getDetectedCharset();

		// (5)
		detector.reset();
		
		return encoding;
	}

}
