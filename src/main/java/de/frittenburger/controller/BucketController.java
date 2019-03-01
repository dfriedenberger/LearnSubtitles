package de.frittenburger.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.api.BucketApi;
import de.frittenburger.impl.RepositoryServiceImpl;
import de.frittenburger.impl.UploadRepositoryImpl;
import de.frittenburger.interfaces.RepositoryService;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.Bucket;
import de.frittenburger.model.BucketData;
import de.frittenburger.model.BucketInfo;
import de.frittenburger.model.UploadBucket;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/v1/")
public class BucketController implements BucketApi {
	
	private static final Logger logger = LogManager.getLogger(PageController.class);

	private static final UploadRepository repository = UploadRepositoryImpl.getInstance();
	private static final RepositoryService repositoryService = new RepositoryServiceImpl(repository);

	 @Override
	public ResponseEntity<List<Bucket>> getBuckets() {
		 
		    List<Bucket> bucketList = new ArrayList<>();
			
				
				for(String id : repository.readBucketIds())
				{
					Bucket bucket = new Bucket();
					bucket.setId(id);
					bucket.setName(id);

					bucketList.add(bucket);

					try {
					
						UploadBucket uploadBucket =  repository.readBucket(id);
						File[] files = repository.readFiles(uploadBucket);
						for(File file : files)
						{
							bucket.addFilesItem(file.getName());
						}
						BucketInfo bucketInfo = repositoryService.getBucketInfo(id);
						bucket.setName(bucketInfo.getTitle());

					} catch (Exception e) {
						logger.error(e);
					}
				}
				
			
			return ok(bucketList);
	}
	
    @Override
	public ResponseEntity<Void> uploadFile(String bucketId,
			@Valid MultipartFile file) {
    	
    	try {

			if (logger.isInfoEnabled()) {
				logger.info("call upload {} ", bucketId);
			}
			
			UploadBucket bucket = repository.readBucket(bucketId);
		
			//String mimeType = file.getContentType();
			String filename = file.getOriginalFilename();
			byte[] bytes = file.getBytes();

			repository.createFile(bucket, filename, bytes);

			if (logger.isInfoEnabled()) {
				logger.info("saved " + filename + " bytes=" + bytes.length
						+ " bucket=" + bucket);
			}

		
		} catch (Exception e) {
			logger.error(e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);

	} 
	
    @Override
	public ResponseEntity<Void> uploadData(String bucketId,
			@Valid BucketData bucket) {
    	try {
		
			UploadBucket uploadBucket = repository.readBucket(bucketId);
			BucketInfo bucketInfo = new BucketInfo();
			bucketInfo.setTitle(bucket.getTitle());
			bucketInfo.setDescription(bucket.getDescription());
			String info = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(bucketInfo);
			repository.updateFile(uploadBucket, "info.json", info.getBytes());

				
		} catch (Exception e) {
			logger.error(e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

    
    @Override
    public ResponseEntity<Void> deleteFile(String bucketId, String filename) {
    	try {

			if (logger.isInfoEnabled()) {
				logger.info("call upload {} ", bucketId);
			}
			
			UploadBucket bucket = repository.readBucket(bucketId);
		
		
			repository.deleteFile(bucket, filename);

			if (logger.isInfoEnabled()) {
				logger.info("deleted " + filename + " bucket=" + bucket);
			}

		
		} catch (Exception e) {
			logger.error(e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
    
    
}
