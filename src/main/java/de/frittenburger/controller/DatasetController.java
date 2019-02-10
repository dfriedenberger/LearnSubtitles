package de.frittenburger.controller;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.api.DatasetApi;
import de.frittenburger.impl.RepositoryServiceImpl;
import de.frittenburger.impl.UploadRepositoryImpl;
import de.frittenburger.interfaces.RepositoryService;
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
	private static final RepositoryService repositoryService = new RepositoryServiceImpl(repository);

	@Override
	public ResponseEntity<List<BucketMetadata>> getDatasets()  {

		
		
			List<BucketMetadata> list = new ArrayList<BucketMetadata>();
			
			
			for(String bucketId : repository.readBucketIds())
			{
				try
				{
	
					BucketMetadata md = repositoryService.getBucketMetadata(bucketId);
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

			 HttpHeaders headers = new HttpHeaders();
	         
	         if(filename.endsWith(".json"))
	         {
	        	 headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	         }
	         byte data[] = repository.readFile(bucket, filename);
	
			

			 return ok().headers(headers)
			            .contentLength(data.length)
			            .body(new ByteArrayResource(data));
			
			
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
