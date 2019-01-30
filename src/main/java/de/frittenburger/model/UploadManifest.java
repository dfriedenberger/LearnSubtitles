package de.frittenburger.model;

import java.io.File;

public class UploadManifest {

	private final File path;
	private final String datapath;
	private final String algorithm;

	public UploadManifest(File path, String datapath,String algorithm) {
		this.path = path;
		this.datapath = datapath;
		this.algorithm = algorithm;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public File getPath() {
		return path;
	}

	public String add(String cksum, String filename) {
		return cksum + " "+datapath+filename+"\n";
	}

}
