package de.frittenburger.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import de.frittenburger.api.ComposerApi;
import de.frittenburger.impl.ComposerEngine;
import de.frittenburger.impl.ComposerFactory;
import de.frittenburger.interfaces.ComposerJob;
import de.frittenburger.model.Job;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/v1/")
public class ComposerController implements ComposerApi {

	
	private static final Logger logger = LogManager.getLogger(ComposerController.class);
	
	
	
	@Override
	public ResponseEntity<List<Job>> getJobs() {

		List<Job> jobs = new ArrayList<Job>();
		
		
		for(ComposerJob cjob : ComposerEngine.getInstance().list())
		{
			Job job = new Job();
			job.setId(cjob.getId());
			job.setBucketId(cjob.getBucketId());
			job.setType(cjob.getType());
			job.setState(cjob.getState());
			job.setDuration(cjob.getDuration());
			jobs.add(job);
		}
		
		return ok(jobs);
	}
	
	@Override
	public ResponseEntity<Job> generate(String bucketId, String artefact) {
		
		
		try
		{
			ComposerJob cjob = ComposerFactory.createJobForArtefact(bucketId,artefact);
			//Process bucket
			ComposerEngine.getInstance().enqueue(cjob);
			
			
			Job job = new Job();
			job.setId(cjob.getId());
			job.setBucketId(cjob.getBucketId());
			job.setType(cjob.getType());
			job.setState(cjob.getState());
			job.setDuration(cjob.getDuration());
			return ok(job);
			
		} 
		catch(IOException e)
		{
			logger.error(e);
			
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	
}
