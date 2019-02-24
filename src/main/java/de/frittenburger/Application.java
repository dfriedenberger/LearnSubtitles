package de.frittenburger;

import java.io.IOException;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;



@SpringBootApplication
public class Application implements WebMvcConfigurer {

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
	
	
	 @Bean
	    public MessageSource messageSource() {
	        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	        messageSource.setBasename("messages");
	        messageSource.setDefaultEncoding("UTF-8");
	        return messageSource;
	    }

	  @Bean
	    public LocaleResolver localeResolver() {
	        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
	        sessionLocaleResolver.setDefaultLocale(Locale.ENGLISH);
	        return sessionLocaleResolver;
	    }
	  
	  @Bean
	  public LocaleChangeInterceptor localeChangeInterceptor() {
	      LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	      lci.setParamName("lang");
	      return lci;
	  }
	  
	 @Override
	  public void addInterceptors(InterceptorRegistry registry) {
	      registry.addInterceptor(localeChangeInterceptor());
	  }
	 
}
