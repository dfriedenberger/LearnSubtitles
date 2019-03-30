package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.interfaces.GameService;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.UploadBucket;

public class GameJob extends BaseJob {

	private final Logger logger = LogManager.getLogger(GameJob.class);

	private final UploadRepository repository;
	private final GameService gameService;
	
	public GameJob(String id, UploadRepository repository,
			String bucketId, GameService gameService) {
		super(id,bucketId,"translation");
		this.repository = repository;
		this.gameService = gameService;
	}
	
	@Override
	public void run() throws IOException {
		
		UploadBucket bucket = repository.readBucket(getBucketId());
		if(bucket == null)
			throw new IOException("bucket not found");
		
		logger.info("Create DataSet "+gameService.getName());
		File mergeFile = new File(bucket.getPayload(),"merge_gen.txt");
		File datasetFile = new File(bucket.getPayload(),gameService.getName()+"_gen.json");
		gameService.generateDataSet(mergeFile,datasetFile);
		
		
	}
}
