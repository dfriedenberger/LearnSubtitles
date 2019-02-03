package de.frittenburger.interfaces;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Set;

import de.frittenburger.model.UploadBucket;

public interface UploadRepository {

	String generateRandomID();

	//CRUD
	Set<String> readBucketIds();

	UploadBucket readBucket(String bucketId) throws IOException;



	UploadBucket createBucket(String bucketId) throws IOException;

	
	
	void createFile(UploadBucket bucket, String filename, byte[] bytes) throws GeneralSecurityException, IOException;
	void createManifest(UploadBucket bucket, File file) throws GeneralSecurityException, IOException;

	File[] readFiles(UploadBucket bucket);

	void createMetadata(UploadBucket bucket, String[] key, String[] value) throws IOException;

	byte[] readFile(UploadBucket bucket, String filename) throws IOException;




}
