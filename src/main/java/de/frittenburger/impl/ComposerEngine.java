package de.frittenburger.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.interfaces.ComposerService;


public class ComposerEngine {

	    @SuppressWarnings("unused")
		private final Logger logger = LogManager.getLogger(ComposerEngine.class);
		private List<ComposerService> workers = new ArrayList<ComposerService>();

		private ComposerEngine () {

			
		}
		public int getState(String bucketId) {
			
			synchronized(workers)
			{
				for(ComposerService s : workers)
				{
					if(!s.getBucketId().equals(bucketId))
						continue;
					return s.getState();
				}
			}
			return -1;
		}
		
		public void enqueue(ComposerService composerService) {
			
			synchronized(workers)
			{
				workers.add(composerService);
				new Thread(composerService).start();

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
