package de.frittenburger.controller;


import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import de.frittenburger.impl.ComposerEngine;
import de.frittenburger.impl.ComposerServiceImpl;
import de.frittenburger.impl.DetectorServiceImpl;
import de.frittenburger.impl.UnzipServiceImpl;
import de.frittenburger.impl.UploadRepositoryImpl;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.UploadBucket;

@Controller
public class PageController {

	private static final Logger logger = LogManager.getLogger(PageController.class);

	private static final UploadRepository repository = new UploadRepositoryImpl(
			"upload");

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {

		model.put("test", "xxx");

		if (logger.isInfoEnabled()) {
			logger.info("call Welcome");
		}

		return "welcome";
	}

	@RequestMapping("/create")
	public String create(Map<String, Object> model) {

		model.put("bucket", repository.generateRandomID());

		if (logger.isInfoEnabled()) {
			logger.info("call Create {}" , model.get("bucket"));
		}

		return "create";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/upload", 
	method = RequestMethod.POST)
	public ResponseEntity uploadFile(MultipartHttpServletRequest request) {

		try {

			String bucketId = request.getParameter("bucket");
			if (logger.isInfoEnabled()) {
				logger.info("call upload {} ", bucketId);
			}
			UploadBucket bucket = repository.getBucket(bucketId);

			if (bucket == null) {
				// create
				bucket = repository.createBucket(bucketId);
			}

			Iterator<String> itr = request.getFileNames();

			while (itr.hasNext()) {
				String uploadedFile = itr.next();
				MultipartFile file = request.getFile(uploadedFile);
				//String mimeType = file.getContentType();
				String filename = file.getOriginalFilename();
				byte[] bytes = file.getBytes();

				repository.createFile(bucket, filename, bytes);

				if (logger.isInfoEnabled()) {
					logger.info("saved " + filename + " bytes=" + bytes.length
							+ " bucket=" + bucket);
				}

			}
		} catch (Exception e) {
			logger.error(e);
			return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("{}", HttpStatus.OK);
	}

	@RequestMapping(value = "/process/{bucketId}", 
			produces = { "application/json" }, 
			method = RequestMethod.GET)
	ResponseEntity<Object> getScenario(@PathVariable("bucketId") String bucketId) {

		Map<String, Object> result = new TreeMap<String, Object>();

		result.put("message", "unknown error");
		result.put("bucket", bucketId);

		try {
			if(repository.getBucket(bucketId) == null)
			{
				result.put("message", "bucket not exists");
			}
			else 
			{
				int state = ComposerEngine.getInstance().getState(bucketId);
			    result.put("state", state);

				switch(state)
				{
					case -1: //not found
						//Process bucket
						ComposerEngine.getInstance().enqueue(new ComposerServiceImpl(repository,bucketId,
								new UnzipServiceImpl(),new DetectorServiceImpl()));
					    result.put("message", "ok");
						break;
					case 0: //init
					    result.put("message", "ok");
						break;
					case 99: //ready
					    result.put("message", "ready");
						break;
					default:
						 result.put("message", "composer error");
						 break;
				}
			
			}
		
		} catch (Exception e) {
			logger.error(e);
		}
		return new ResponseEntity<Object>(result, HttpStatus.OK);

	}
}