package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.interfaces.EncodingDetectorService;
import de.frittenburger.interfaces.LanguageDetectorService;
import de.frittenburger.interfaces.SrtMergerService;
import de.frittenburger.srt.DefaultFilter;
import de.frittenburger.srt.SrtCluster;
import de.frittenburger.srt.SrtMergeWriter;
import de.frittenburger.srt.SrtMerger;
import de.frittenburger.srt.SrtReader;

public class SrtMergerServiceImpl implements SrtMergerService {

	private EncodingDetectorService encodingDetectorService = new EncodingDetectorServiceImpl();
	private LanguageDetectorService languageDetectorService = new LanguageDetectorServiceImpl();
	
	private final Logger logger = LogManager.getLogger(SrtMergerServiceImpl.class);
	private final SrtMerger srtMerger;

	public SrtMergerServiceImpl(SrtMerger srtMerger)
	{
		this.srtMerger = srtMerger;
	}
	@Override
	public void merge(File[] srtFiles, File mergeFile) throws IOException {

		if(srtFiles.length != 2)
		{
			throw new IOException("only can process 2 files");
		}
		
		SrtReader[] srtReader = new SrtReader[2];
		
		
		for(int i = 0;i < srtFiles.length;i++)
		{
			byte[] data = Files.readAllBytes(srtFiles[i].toPath());
			String encoding = encodingDetectorService.detect(data);
			
			if(encoding == null)
				throw new IllegalArgumentException("could not detect encoding "+srtFiles[i].getName());
			
			
			String language = languageDetectorService.detect(new String(data,encoding));
			if(language == null)
				throw new IllegalArgumentException("could not detect language "+srtFiles[i].getName());
			
			//read 
			if (logger.isInfoEnabled()) {
				logger.info("call reader {} {} {}" , srtFiles[i].getName(), language, encoding);
			}

			//str - reader
			srtReader[i] = new SrtReader();
			srtReader[i].load(language,srtFiles[i].getPath(),new DefaultFilter(),encoding);
			//dump			
		}
		
		//Merge and add to Bucket
		List<SrtCluster> clusterList = srtMerger.merge(srtReader[0],srtReader[1]);
		
		
		
		SrtMergeWriter writer = new SrtMergeWriter(mergeFile);

		for(SrtCluster c : clusterList)
		{
			writer.write(c);
		}
		writer.close();
		
		
		
	}

}
