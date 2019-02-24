package de.frittenburger.controller;



import java.security.GeneralSecurityException;
import java.util.List;
import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.frittenburger.impl.RepositoryServiceImpl;
import de.frittenburger.impl.UploadRepositoryImpl;
import de.frittenburger.impl.UserRepositoryImpl;
import de.frittenburger.interfaces.RepositoryService;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.BucketMetadata;

@Controller
public class PageController {

	private static final Logger logger = LogManager.getLogger(PageController.class);

	private static final UploadRepository repository = UploadRepositoryImpl.getInstance();
	private static final RepositoryService repositoryService = new RepositoryServiceImpl(repository);

	private void defaultValues(Map<String, Object> model) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			String hash = UserRepositoryImpl.hash(username);
			model.put("userid", hash);
		} catch (GeneralSecurityException e) {
			logger.error(e);
		}

	}
	
	
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {

		defaultValues(model);
		
		if (logger.isInfoEnabled()) {
			logger.info("call Welcome");
		}
		
	    return "welcome";
	}


	
	



	@RequestMapping("/create")
	public String create(Map<String, Object> model) {

		defaultValues(model);

		model.put("bucket", repository.generateRandomID());

		if (logger.isInfoEnabled()) {
			logger.info("call Create {}" , model.get("bucket"));
		}

		return "create";
	}

	 @RequestMapping(value = "/play/{bucketId}",
		        method = RequestMethod.GET)
	 public String play(Map<String, Object> model, @PathVariable("bucketId") String bucketId) throws IOException {

		defaultValues(model);

		model.put("bucketId", bucketId);

		BucketMetadata md = repositoryService.getBucketMetadata(bucketId);
		
		List<String> languages = md.getLanguages();
		model.put("language0", languages.get(0));
		model.put("language1", languages.get(1));
		model.put("title", md.getTitle());

		
		if (logger.isInfoEnabled()) {
			logger.info("call Create {} {}" , bucketId);
		}

		return "play";
	}

	
}