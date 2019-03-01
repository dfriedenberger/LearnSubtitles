package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.interfaces.ComposerJob;
import de.frittenburger.interfaces.TranslationService;
import de.frittenburger.interfaces.UnzipService;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.UploadBucket;

public class TranslationJob extends BaseJob {

	private final Logger logger = LogManager.getLogger(TranslationJob.class);

	private final UploadRepository repository;
	private final TranslationService translationService;
	
	public TranslationJob(String id, UploadRepository repository,
			String bucketId, TranslationService translationService) {
		super(id,bucketId,"translation");
		this.repository = repository;
		this.translationService = translationService;
	}
	
	@Override
	public void run() throws IOException {
		
		UploadBucket bucket = repository.readBucket(getBucketId());
		if(bucket == null)
			throw new IOException("bucket not found");
		
		//TranslationFile
		logger.info("Create Translationfile");
		File mergeFile = new File(bucket.getPayload(),"merge_gen.txt");
		File translationFile = new File(bucket.getPayload(),"translation_gen.json");
		translationService.translate(mergeFile,translationFile);
		
	}
}
