package de.frittenburger.model;

import java.io.File;

public class UploadBucket {

	private final File path;
	private final UploadBagitInfo metadata;

	public UploadBucket(File path) {
		this.path = path;
		this.metadata = new UploadBagitInfo(new File(path,"bag-info.txt")); 
	}

	public File getPath() {
		return path;
	}
	
	public File getPayload() {
		return new File(path,"data");
	}
	
	public UploadBagitInfo getMetadata() {
		return metadata;
	}

}
