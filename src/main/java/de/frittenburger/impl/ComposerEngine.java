package de.frittenburger.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.interfaces.ComposerJob;


public class ComposerEngine {

	    @SuppressWarnings("unused")
		private final Logger logger = LogManager.getLogger(ComposerEngine.class);
		private List<ComposerJob> jobs = new ArrayList<ComposerJob>();
		private List<ComposerWorker> workers = new ArrayList<ComposerWorker>();
		
		private ComposerEngine () {	}
		
	
		
		public List<ComposerJob> list() {
			return jobs;
		}

		public void enqueue(ComposerJob job) {
			
			synchronized(workers)
			{
				ComposerWorker worker = new ComposerWorker(job);
				workers.add(worker);
				jobs.add(job);
				job.setState("init");
				worker.start();
			}
			
		}
		
		private static ComposerEngine instance = null;
		
		public static synchronized ComposerEngine getInstance () {
		    if (ComposerEngine.instance == null) {
		    	ComposerEngine.instance = new ComposerEngine();
		    }
		    return ComposerEngine.instance;
		}
		
	



		
		
		
}
