package de.frittenburger.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.interfaces.ComposerJob;

public class ComposerWorker extends Thread {

	private final Logger logger = LogManager.getLogger(ComposerWorker.class);
	private final ComposerJob job;

	public ComposerWorker(ComposerJob job) {
		this.job = job;
	}
	
	@Override
	public void run() {
		
		try
		{
			job.setState("running");
			job.run();
			job.setState("done");
		}
		catch(Exception e)
		{
			logger.error(e);
			job.setState("exception");
		}
		job.stop();
	}

}
