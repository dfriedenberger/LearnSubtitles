package de.frittenburger.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.api.CreateApi;
import de.frittenburger.impl.ComposerEngine;
import de.frittenburger.impl.ComposerServiceImpl;
import de.frittenburger.impl.SrtMergerServiceImpl;
import de.frittenburger.impl.UnzipServiceImpl;
import de.frittenburger.impl.UploadRepositoryImpl;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.BucketCommand;
import de.frittenburger.model.BucketState;
import de.frittenburger.model.UploadBucket;



@RestController
@RequestMapping("api/v1/")
public class CreateController implements CreateApi {

	private static final Logger logger = LogManager.getLogger(PageController.class);

	private static final UploadRepository repository = UploadRepositoryImpl.getInstance();
	
	@Override
	public ResponseEntity<Void> uploadFile(String bucketId,
			@Valid MultipartFile file) {
		
		try {

			if (logger.isInfoEnabled()) {
				logger.info("call upload {} ", bucketId);
			}
			UploadBucket bucket = repository.readBucket(bucketId);

			if (bucket == null) {
				// create
				bucket = repository.createBucket(bucketId);
			}

		
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
	public ResponseEntity<Void> startProcessing(@Valid BucketCommand bucketCommand) {
		try {
			if(bucketCommand.getId() == null)
			{
				throw new IllegalArgumentException("bucketId");
			}
			UploadBucket bucket = repository.readBucket(bucketCommand.getId());
			if(bucket == null)
			{
				throw new NoSuchElementException(bucketCommand.getId());
			}
			
			int state = ComposerEngine.getInstance().getState(bucketCommand.getId());
				
			switch(state)
			{
				case -1: //not found
					Map<String,String> map = new HashMap<String,String>();
					map.put("title", bucketCommand.getTitle());
					map.put("description", bucketCommand.getDescription());
					String info = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
					repository.createFile(bucket, "info.json", info.getBytes());

					//Process bucket
					ComposerEngine.getInstance().enqueue(
							new ComposerServiceImpl(repository,
									bucketCommand.getId(),
							new UnzipServiceImpl(),new SrtMergerServiceImpl()));
					break;
				default:
					throw new RuntimeException("bucketId "+bucketCommand.getId()+" yet started");
			}
		
		} catch (Exception e) {
			logger.error(e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<BucketState> getState(String bucketId) {
		BucketState bucketState = new BucketState();
		
		bucketState.setId(bucketId);
		

		try {
			if(repository.readBucket(bucketId) == null)
			{
				throw new IllegalArgumentException("bucketId");
			}
			
			int state = ComposerEngine.getInstance().getState(bucketId);
			bucketState.setState(""+state);
			
		} catch (Exception e) {
			logger.error(e);
			bucketState.setState(e.getMessage());
			return new ResponseEntity<BucketState>(bucketState,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<BucketState>(bucketState,HttpStatus.OK);
	}
	
}
