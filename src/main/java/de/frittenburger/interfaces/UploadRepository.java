package de.frittenburger.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import de.frittenburger.model.UploadBucket;

public interface UploadRepository {

	String generateRandomID();

	//CRUD
	UploadBucket createBucket(String bucketId) throws IOException;

	Set<String> readBucketIds();

	UploadBucket readBucket(String bucketId) throws IOException;

	void createFile(UploadBucket bucket, String filename, byte[] bytes) throws  IOException;

	File[] readFiles(UploadBucket bucket);

	byte[] readFile(UploadBucket bucket, String filename) throws IOException;

	void updateFile(UploadBucket bucket, String filename, byte[] bytes) throws  IOException;

	void deleteFile(UploadBucket bucket, String filename) throws IOException;

	void createMetadata(UploadBucket bucket, String[] key, String[] value) throws IOException;


}
