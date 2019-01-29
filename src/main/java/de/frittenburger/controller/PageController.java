package de.frittenburger.controller;


import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {

    private static final Logger logger = LogManager.getLogger(PageController.class);

	
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		
		
		model.put("test","xxx");

		if (logger.isInfoEnabled()) {
	          logger.info("call Welcome");
	    }	
		
		
		return "welcome";
	}
	
	
	
	
	
	
	
}