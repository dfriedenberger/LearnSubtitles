package de.frittenburger.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.api.DatasetApi;
import de.frittenburger.impl.UploadRepositoryImpl;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.BucketMetadata;
import de.frittenburger.model.UploadBucket;
import de.frittenburger.srt.SrtCluster;
import de.frittenburger.srt.SrtMergeReader;
import static org.springframework.http.ResponseEntity.ok;



@RestController
@RequestMapping("api/v1/")
public class DatasetController implements DatasetApi {

	private static final Logger logger = LogManager.getLogger(PageController.class);

	private static final UploadRepository repository = UploadRepositoryImpl.getInstance();

	@Override
	public ResponseEntity<List<BucketMetadata>> getDatasets()  {

		
		
			List<BucketMetadata> list = new ArrayList<BucketMetadata>();
			
			
			for(String bucketId : repository.readBucketIds())
			{
				try
				{
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
					list.add(md);

				}
				catch(Exception e)
				{
					logger.error(e);
				}
				
			}
			return ok(list);
	
		//return new ResponseEntity<List<BucketMetadata>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@Override
	public ResponseEntity<Resource> getDataset(String bucketId) {
		try
		{
			UploadBucket bucket = repository.readBucket(bucketId);
	
			byte text[] = repository.readFile(bucket, "merge_gen.txt");

			SrtMergeReader reader = new SrtMergeReader(text);
			
			List<SrtCluster> clusters = reader.read();
	
			byte[] data = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsBytes(clusters);
			
			
			return ok(new ByteArrayResource(data));
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	public ResponseEntity<Resource> getFile(String bucketId, String filename) {
		
		try
		{
			UploadBucket bucket = repository.readBucket(bucketId);
	
			byte data[] = repository.readFile(bucket, filename);
	
			return ok(new ByteArrayResource(data));
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
