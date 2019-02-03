package de.frittenburger.model;

import java.io.File;

public class UploadBagitInfo {

	private final File path;

	public UploadBagitInfo(File path) {
		this.path = path;
	}

	public String add(String[] key, String[] value) {
		
		if(key == null)
			throw new IllegalArgumentException("key");
		if(value == null)
			throw new IllegalArgumentException("value");
		if(key.length != value.length)
			throw new IllegalArgumentException("key/value");
		
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < key.length;i++)
		{
			buffer.append(key[i]+": " +value[i]);
			buffer.append("\n");
		}
		return buffer.toString();
	}

	public File getPath() {
		return path;
	}
	
}
