package de.frittenburger.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.io.IOException;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.frittenburger.api.DatabaseApi;
import de.frittenburger.impl.UserRepositoryImpl;

@RestController
@RequestMapping("api/v1/")
public class DatabaseController implements DatabaseApi {

	private static final Logger logger = LogManager.getLogger(DatabaseController.class);

	@Override
	public ResponseEntity<Object> writeUserData(String key, @Valid Object data) {

		String userId = SecurityContextHolder.getContext().getAuthentication().getName();

		if (logger.isInfoEnabled()) {
			logger.info("{} {} = {}",userId,key,data);
		}
		
		try {
			
			UserRepositoryImpl.getInstance().update(userId, key, data);
			return ok(data);

		} catch (IOException e) {
			logger.error(e);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@Override
	public ResponseEntity<Object> readUserData(String key) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();

		try {
			
			Object obj = UserRepositoryImpl.getInstance().read(userId, key);

			if (logger.isInfoEnabled()) {
				logger.info("{} {} = {}",userId,key,obj);
			}
			
			
			return ok(obj);
		} catch (IOException e) {
			logger.error(e);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
