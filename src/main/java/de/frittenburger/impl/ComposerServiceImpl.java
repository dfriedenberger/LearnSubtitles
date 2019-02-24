package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.interfaces.ComposerService;
import de.frittenburger.interfaces.SrtMergerService;
import de.frittenburger.interfaces.TranslationService;
import de.frittenburger.interfaces.UnzipService;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.UploadBucket;

public class ComposerServiceImpl implements ComposerService {

	
	private final Logger logger = LogManager.getLogger(ComposerServiceImpl.class);

	private final UploadRepository repository;
	
	private final UnzipService unzipService;
	private final SrtMergerService srtMergerService;
	private final TranslationService translationService;

	private final String bucketId;
	private int state = 0;



	public ComposerServiceImpl(UploadRepository repository, String bucketId,
			UnzipService unzipService,SrtMergerService srtMergerService,
			TranslationService translationService) {
		this.repository = repository;
		this.bucketId = bucketId;
		this.unzipService = unzipService;
		this.srtMergerService = srtMergerService;
		this.translationService = translationService;
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
			
			UploadBucket bucket = repository.readBucket(bucketId);
			if(bucket == null)
				throw new IOException("bucket not found");
			
			
			File files[] = repository.readFiles(bucket);
		
			List<File> srtFiles = new ArrayList<File>();
			
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
					repository.createManifest(bucket, files[i]);
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
			repository.createManifest(bucket, mergeFile);
			
			//TranslationFile
			logger.info("Create Translationfile");
			File translationFile = new File(bucket.getPayload(),"translation_gen.json");
			translationService.translate(mergeFile,translationFile);
			repository.createManifest(bucket, translationFile);
			Thread.sleep(60 * 1000);
			state = 99;
			logger.info("Ready");
		} catch (Exception e) {
			logger.error(e);
			state = -2;
		}
		
	}


}
