package de.frittenburger.interfaces;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import de.frittenburger.model.UploadBucket;

public interface UploadRepository {

	String generateRandomID();

	//CRUD

	UploadBucket getBucket(String bucketId) throws IOException;



	UploadBucket createBucket(String bucketId) throws IOException;

	
	
	void createFile(UploadBucket bucket, String filename, byte[] bytes) throws GeneralSecurityException, IOException;
	void createManifest(UploadBucket bucket, File file) throws GeneralSecurityException, IOException;

	File[] readFiles(UploadBucket bucket);



}
