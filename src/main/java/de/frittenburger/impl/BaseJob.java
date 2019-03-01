package de.frittenburger.impl;

import java.util.Date;

import de.frittenburger.interfaces.ComposerJob;
import de.frittenburger.interfaces.UploadRepository;

public abstract class BaseJob implements ComposerJob  {

	
	private final String id;
	private final String bucketId;
	private final String type;
	private String state = "unknown";
	private boolean running = false;
	private long stop = -1;
	private long start = 1;

	public BaseJob(String id, String bucketId, String type) {
		this.id = id;
		this.bucketId = bucketId;
		this.type = type;
		this.start = new Date().getTime();
		this.running = true;
	}

	@Override
	public String getBucketId() {
		return bucketId;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public long getDuration()
	{
		if(running)
			return new Date().getTime() - start;
		return stop - start;
	}
	
	@Override
	public void stop()
	{
		running = false;
		stop = new Date().getTime();
	}
}
