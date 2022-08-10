package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.interfaces.RepositoryService;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.BucketInfo;
import de.frittenburger.model.BucketMetadata;
import de.frittenburger.model.UploadBucket;

public class RepositoryServiceImpl implements RepositoryService {

	private final UploadRepository repository;

	public RepositoryServiceImpl(UploadRepository repository) {
		this.repository = repository;
	}

	@Override
	public BucketInfo getBucketInfo(String bucketId) throws IOException {
		
		UploadBucket bucket = repository.readBucket(bucketId);
		byte src[] = repository.readFile(bucket, "info.json");
		
		return new ObjectMapper().readValue(src, BucketInfo.class);
	}
	
	@Override
	public BucketMetadata getBucketMetadata(String bucketId) throws IOException {
		
		UploadBucket bucket = repository.readBucket(bucketId);
		byte src[] = repository.readFile(bucket, "info.json");
		
		BucketInfo info = new ObjectMapper().readValue(src, BucketInfo.class);
		
		BucketMetadata md = new BucketMetadata();
		md.setId(bucketId);
		md.setTitle(info.getTitle());
		
		String desc = info.getDescription();
		if(desc.length() > 140) 
		{
			int i = desc.lastIndexOf(" ", 140);
			desc = desc.substring(0, i + 1)+"...";
		}
		
		md.setDescription(desc);
		
		for(File file : repository.readFiles(bucket))
		{
			String name = file.getName();
			
			if(name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"))
				md.setImage("/api/v1/dataset/"+bucketId+"/"+name);
		}
		
		
		

		
		
		List<String> languages = new ArrayList<String>();
		languages.add("de");
		languages.add("es");

		md.setCount(512);
		md.setLanguages(languages);
		
		return md;
	}

	



	
}
