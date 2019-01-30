package de.frittenburger.model;

import java.io.File;

public class UploadBagitInfo {

	private final File path;
	private final StringBuffer buffer = new StringBuffer();

	public UploadBagitInfo(File path) {
		this.path = path;
	}

	public void add(String key, String value) {
		buffer.append(key+": " +value);
		buffer.append("\n");
	}

	public File getPath() {
		return path;
	}

	public String getData() {
		return buffer.toString();
	}

	
}
