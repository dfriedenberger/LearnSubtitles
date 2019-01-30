package de.frittenburger.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadBucket {

	private final File path;
	private final UploadBagitInfo metadata;
	private final UploadManifest manifest;

	public UploadBucket(File path, String id) {
		this.path = path;
		this.metadata = new UploadBagitInfo(new File(path,"bag-info.txt")); 
		this.metadata.add("Id",id);
		this.metadata.add("Creation-Date",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		this.manifest = new UploadManifest(new File(path,"manifest-md5.txt"),"data/","MD5");
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

	public UploadManifest getManifest() {
		return manifest;
	}
}