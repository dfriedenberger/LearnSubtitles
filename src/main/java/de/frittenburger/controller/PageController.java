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
import de.frittenburger.impl.SrtMergerServiceImpl;
import de.frittenburger.impl.UnzipServiceImpl;
import de.frittenburger.impl.UploadRepositoryImpl;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.UploadBucket;

@Controller
public class PageController {

	private static final Logger logger = LogManager.getLogger(PageController.class);

	private static final UploadRepository repository = UploadRepositoryImpl.getInstance();

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {

		model.put("test", "xxx");

		if (logger.isInfoEnabled()) {
			logger.info("call Welcome");
		}

		return "welcome";
	}

	@RequestMapping(value = "/{language}/",
	        method = RequestMethod.GET)
	public String index(Map<String, Object> model, @PathVariable("language") String language) {

		model.put("language", language);

		if (logger.isInfoEnabled()) {
			logger.info("call Welcome {}", language);
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

	 @RequestMapping(value = "/play/{language}/{bucketId}",
		        method = RequestMethod.GET)
	 public String play(Map<String, Object> model, @PathVariable("language") String language, @PathVariable("bucketId") String bucketId) {

		model.put("bucketId", bucketId);
		model.put("language", language);

		if (logger.isInfoEnabled()) {
			logger.info("call Create {} {}" ,language, bucketId);
		}

		return "play";
	}

	
}