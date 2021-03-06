package de.frittenburger.impl;

import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.interfaces.ComposerJob;
import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.srt.SrtMergerImpl;


public class ComposerFactory {

	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(ComposerFactory.class);
	
	private static final UploadRepository repository = UploadRepositoryImpl.getInstance();
	
	
	public static ComposerJob createJobForArtefact(final String bucketId,
			final String artefact) throws IOException {
		
		switch(artefact)
		{
		case "merge":
			return new MergeJob(UUID.randomUUID().toString(),repository,bucketId,new UnzipServiceImpl(),
					new SrtMergerServiceImpl(new SrtMergerImpl()));
		case "translation":
			return new GameJob(UUID.randomUUID().toString(),repository,bucketId,new CardGameService());
		}
		
		throw new IOException("unknown artefact: "+artefact);
		
	}

}
