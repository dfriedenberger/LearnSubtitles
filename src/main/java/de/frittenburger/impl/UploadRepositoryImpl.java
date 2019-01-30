package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.UploadBucket;
import de.frittenburger.model.UploadManifest;

public class UploadRepositoryImpl implements UploadRepository {

	private final File root;
	private final Map<String,UploadBucket> buckets = new HashMap<String,UploadBucket>();

	public UploadRepositoryImpl(String path) {
		
		this.root = new File(path);
		if(!this.root.isDirectory())
			throw new IllegalArgumentException(path + " has to be a directory");

		//read all Buckets
		
		
	}

	@Override
	public String generateRandomID() {
		return UUID.randomUUID().toString();
	}

	
	@Override
	public UploadBucket getBucket(String bucketId) {
		
		if(bucketId == null || bucketId.trim().isEmpty())
			throw new IllegalArgumentException("bucketid");
		
		UUID.fromString(bucketId);
		
		
		if(!buckets.containsKey(bucketId))
			return null; //Not exists, contains no upload's
		
		return buckets.get(bucketId);

	}
	
	@Override
	public UploadBucket createBucket(String bucketId) throws IOException {
		
		
		if(bucketId == null || bucketId.trim().isEmpty())
			throw new IllegalArgumentException("bucketid");
		
		if(buckets.containsKey(bucketId))
			throw new IllegalArgumentException("bucket exists");
		
		UUID.fromString(bucketId);
		
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());  
	   

		for(int i = 1;i < 100;i++)
		{
			File bucketPath = new File(root,date+"-"+i);
			if(bucketPath.exists()) continue;
			
			//create
			UploadBucket bucket = new UploadBucket(bucketPath,bucketId);
			bucket.getPath().mkdir();
			Files.write( bucket.getMetadata().getPath().toPath(), bucket.getMetadata().getData().getBytes(), StandardOpenOption.CREATE_NEW);
			Files.write( bucket.getManifest().getPath().toPath(), new byte[0], StandardOpenOption.CREATE_NEW);

			bucket.getPayload().mkdir();
			buckets.put(bucketId,bucket);
			return bucket;
		}
		
		
		
		throw new RuntimeException("buckets count limted to 100 per day");
	}

	@Override
	public void createManifest(UploadBucket bucket, File file) throws GeneralSecurityException, IOException {

		UploadManifest manifest = bucket.getManifest();
		String cksum = getCheckSum(file,manifest.getAlgorithm());
		String line = manifest.add(cksum,file.getName());
		Files.write( manifest.getPath().toPath() , line.getBytes(), StandardOpenOption.APPEND);

	}

	@Override
	public void createFile(UploadBucket bucket, String filename, byte[] bytes) throws GeneralSecurityException, IOException {

		if(bytes == null)
			throw new IllegalArgumentException("bytes");

		//Append File
		if(containsAny(filename,ILLEGAL_CHARACTERS))
			throw new IllegalArgumentException("filename");
		
		File out = new File(bucket.getPayload(),filename);
		Files.write( out.toPath() , bytes, StandardOpenOption.CREATE_NEW);

		createManifest(bucket,out);
		
	}

	@Override
	public File[] readFiles(UploadBucket bucket) {
		return bucket.getPayload().listFiles();
	}
	
	
	private String getCheckSum(File file,String typ) throws NoSuchAlgorithmException, IOException {
		    MessageDigest md = MessageDigest.getInstance(typ);
		    byte[] bytes = Files.readAllBytes(file.toPath());
		    md.update(bytes);
		    byte[] digest = md.digest();
		    StringBuilder sb = new StringBuilder();
		    for (byte b : digest) {
		        sb.append(String.format("%02X", b));
		    }
		    return sb.toString();
	}


	private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
	 public static boolean containsAny(String str, char[] searchChars) {
	      if (str == null || str.length() == 0 || searchChars == null || searchChars.length == 0) {
	          return false;
	      }
	      for (int i = 0; i < str.length(); i++) {
	          char ch = str.charAt(i);
	          for (int j = 0; j < searchChars.length; j++) {
	              if (searchChars[j] == ch) {
	                  return true;
	              }
	          }
	      }
	      return false;
	  }

	
	

	


	

}
