package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.interfaces.RepositoryService;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.BucketMetadata;
import de.frittenburger.model.UploadBucket;
import de.frittenburger.srt.SrtCluster;
import de.frittenburger.srt.SrtMergeReader;

public class RepositoryServiceImpl implements RepositoryService {

	private final UploadRepository repository;

	public RepositoryServiceImpl(UploadRepository repository) {
		this.repository = repository;
	}

	@Override
	public BucketMetadata getBucketMetadata(String bucketId) throws IOException {
		
		UploadBucket bucket = repository.readBucket(bucketId);
		byte src[] = repository.readFile(bucket, "info.json");
		
		Map<String,String> map = new ObjectMapper().readValue(src, new TypeReference<HashMap<String,String>>() {});
		
		BucketMetadata md = new BucketMetadata();
		md.setId(bucketId);
		md.setTitle(map.get("title"));
		
		String desc = map.get("description");
		if(desc.length() > 140) 
		{
			int i = desc.lastIndexOf(" ", 140);
			desc = desc.substring(0, i + 1)+"...";
		}
		
		md.setDescription(desc);
		
		for(File file : repository.readFiles(bucket))
		{
			String name = file.getName();
			
			if(name.toLowerCase().endsWith(".jpg"))
				md.setImage("/api/v1/dataset/"+bucketId+"/"+name);
		}
		
		
		
		byte data[] = repository.readFile(bucket, "merge_gen.txt");

		SrtMergeReader reader = new SrtMergeReader(data);
		
		Set<String> languages = new HashSet<String>();
		List<SrtCluster> clusters = reader.read();
		
		for(int i = 0;i < clusters.size() && languages.size() < 2;i++)
		{
			languages.addAll(clusters.get(i).getCounter().keySet());
		}
		md.setCount(clusters.size());
		md.setLanguages(languages.stream().sorted().collect(Collectors.toList()));
		
		return md;
	}



	
}
