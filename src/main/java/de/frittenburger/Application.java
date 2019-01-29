package de.frittenburger;

import java.io.IOException;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);

	public static void main(String[] args) throws IOException {
		
		
		if (logger.isInfoEnabled()) {
	          logger.info("Configure Application");
	    }	
		
		if (logger.isInfoEnabled()) {
	          logger.info("Run Application");
	    }	
		SpringApplication.run(Application.class, args);
	}
}
