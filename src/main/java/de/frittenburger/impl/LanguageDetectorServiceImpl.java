package de.frittenburger.impl;

import io.micrometer.core.instrument.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;




import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;

import de.frittenburger.interfaces.LanguageDetectorService;

public class LanguageDetectorServiceImpl implements LanguageDetectorService {

	
	static
	{
		try
		{
		    List<String> profiles = new ArrayList<>();
	
			String dirname = "profiles/";
		    Enumeration<URL> en = Detector.class.getClassLoader().getResources(
		            dirname);
		    if (en.hasMoreElements()) {
		        URL url = en.nextElement();
		        JarURLConnection urlcon = (JarURLConnection) url.openConnection();
		        try (JarFile jar = urlcon.getJarFile();) {
		            Enumeration<JarEntry> entries = jar.entries();
		            while (entries.hasMoreElements()) {
		                String entry = entries.nextElement().getName();
		                if (entry.startsWith(dirname)) {
		                    try (InputStream in = Detector.class.getClassLoader()
		                            .getResourceAsStream(entry);) {
		                        profiles.add(IOUtils.toString(in));
		                    }
		                }
		            }
		        }
		    }
			DetectorFactory.loadProfile(profiles);

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	
	
	@Override
	public String detect(String text)   {
		
	    
		try {
			Detector detector = DetectorFactory.create(); 
			detector.append(text); 
			ArrayList<Language> pl = detector.getProbabilities();
			
			return pl.get(0).lang;
			
			 
		} catch (LangDetectException e) {
			e.printStackTrace();
		} 
		return null;
	}

}
