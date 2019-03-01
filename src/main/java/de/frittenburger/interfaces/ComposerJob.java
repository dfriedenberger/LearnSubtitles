package de.frittenburger.interfaces;

import java.io.IOException;

public interface ComposerJob {

	String getBucketId();

	String getId();

	String getType();
	
	String getState();

	void setState(String state);
	
	long getDuration();
	
	void stop();

	void run() throws IOException;
}
