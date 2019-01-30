package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Bucket;

import de.frittenburger.controller.PageController;
import de.frittenburger.interfaces.ComposerService;
import de.frittenburger.interfaces.DetectorService;
import de.frittenburger.interfaces.UnzipService;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.DetectMatch;
import de.frittenburger.model.UploadBucket;

public class ComposerServiceImpl implements ComposerService {

	
	private final Logger logger = LogManager.getLogger(ComposerServiceImpl.class);

	private final UploadRepository repository;
	
	private final UnzipService unzipService;
	private final DetectorService detectorService;

	private final String bucketId;
	private int state = 0;

	public ComposerServiceImpl(UploadRepository repository, String bucketId,
			UnzipService unzipService,DetectorService detectorService) {
		this.repository = repository;
		this.bucketId = bucketId;
		this.unzipService = unzipService;
		this.detectorService = detectorService;
	}

	@Override
	public String getBucketId() {
		return bucketId;
	}

	@Override
	public int getState() {
		return state;
	}

	@Override
	public void run() {
		
		try {
			
			UploadBucket bucket = repository.getBucket(bucketId);
			if(bucket == null)
				throw new IOException("bucket not found");
			
			
			File files[] = repository.readFiles(bucket);
			if(files.length != 2)
			{
				throw new IOException("only can process 2 files");
			}
			
			
			for(int i = 0;i < files.length;i++)
			{
				
				DetectMatch m = detectorService.detect(files[i]);
								
				if(m.getExtension().equals(".zip"))
				{
					files[i] = unzipService.extract(files[i],".srt");
					repository.createFile(bucket, files[i]);
					m = detectorService.detect(files[i]);
				}
				
				
				if(!m.getExtension().equals(".srt"))
				{
					throw new IOException("only can process srt files");
				}
				
				
				//read 
				if (logger.isInfoEnabled()) {
					logger.info("call reader {} {}" , m.getLanguage(), m.getEncoding());
				}

				//str - reader
				
				//dump
			}
			
			
			
			//Merge
			
			
			//add to Bucket
			
			
			
			
			
			Thread.sleep(10000);
			state = 99;
		} catch (Exception e) {
			logger.error(e);
			state = -2;
		}
		
	}


}
