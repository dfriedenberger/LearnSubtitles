package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.interfaces.SrtMergerService;
import de.frittenburger.interfaces.UnzipService;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.UploadBucket;

public class MergeJob extends BaseJob {

	private final Logger logger = LogManager.getLogger(TranslationJob.class);
	private final UploadRepository repository;
	private final UnzipService unzipService;
	private final SrtMergerService srtMergerService;

	public MergeJob(String id,UploadRepository repository, String bucketId,
			UnzipService unzipService,
			SrtMergerService srtMergerService) {
		super(id,bucketId,"merge");
		this.repository = repository;
		this.unzipService = unzipService;
		this.srtMergerService = srtMergerService;	
	}

	
	@Override
	public void run() throws IOException {
		
		UploadBucket bucket = repository.readBucket(getBucketId());
		if(bucket == null)
			throw new IOException("bucket not found");
		
		
		File files[] = repository.readFiles(bucket);
	
		Set<File> srtFiles = new HashSet<File>();
		
		for(int i = 0;i < files.length;i++)
		{
			
			String fileName = files[i].getName();
			String extension = "";

			int ix = fileName.lastIndexOf('.');
			if (ix > 0) {
			    extension = fileName.substring(ix+1).toLowerCase();
			}
			
							
			if(extension.equals("zip"))
			{
				files[i] = unzipService.extract(files[i],bucket.getPayload(),".srt");
				extension = "srt";
			}
			
			if(extension.equals("srt"))
			{
				srtFiles.add(files[i]);
			}
		
		}
		
		//Mergefile
		logger.info("Create Mergefile");
		File mergeFile = new File(bucket.getPayload(),"merge_gen.txt");
		srtMergerService.merge(srtFiles.toArray(new File[0]),mergeFile);

	}
}
