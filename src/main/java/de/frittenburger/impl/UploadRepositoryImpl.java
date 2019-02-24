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
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.interfaces.UploadRepository;
import de.frittenburger.model.UploadBagitInfo;
import de.frittenburger.model.UploadBucket;
import de.frittenburger.model.UploadManifest;

public class UploadRepositoryImpl implements UploadRepository {

	private static final Logger logger = LogManager.getLogger(UploadRepositoryImpl.class);

	private static UploadRepository repository = null;
	private final File root;
	private final Map<String,UploadBucket> buckets = new HashMap<String,UploadBucket>();

	private UploadRepositoryImpl(File root) throws IOException {
		
		this.root = root;
		if(!this.root.isDirectory())
			throw new IllegalArgumentException(root + " has to be a directory");

		//read all Buckets
		for(File dir : this.root.listFiles())
		{
			if(!dir.isDirectory()) continue;
			UploadBucket bucket = new UploadBucket(dir);
			String bucketId = null;
			for(String line : Files.readAllLines(bucket.getMetadata().getPath().toPath()))
			{
				int i = line.indexOf(":");
				if(i < 0) continue;
				String key = line.substring(0, i).trim();
				String value = line.substring(i + 1).trim();
				if(key.equals("Id"))
				{
					bucketId = value;
				}
			}
			
			if(bucketId == null)
				throw new NullPointerException("bucketId");
			
			buckets.put(bucketId,new UploadBucket(dir));
		}
	}
	
	public static synchronized   UploadRepository getInstance() {
		if(repository == null)
		{
			try {
				repository = new UploadRepositoryImpl(new File("upload"));
			} catch (IOException e) {
				logger.error(e);
			}
			
		}
		return repository;
	}

	@Override
	public String generateRandomID() {
		return UUID.randomUUID().toString();
	}

	@Override
	public Set<String> readBucketIds() {
		return buckets.keySet();
	}

	@Override
	public UploadBucket readBucket(String bucketId) {
		
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
			UploadBucket bucket = new UploadBucket(bucketPath);
			bucket.getPath().mkdir();
			Files.write( bucket.getMetadata().getPath().toPath(), new byte[0], StandardOpenOption.CREATE_NEW);
			Files.write( bucket.getManifest().getPath().toPath(), new byte[0], StandardOpenOption.CREATE_NEW);

			bucket.getPayload().mkdir();
			buckets.put(bucketId,bucket);
			
			createMetadata(bucket,new String[]{"Id", "Creation-Date"},
					new String[]{ bucketId ,new SimpleDateFormat("yyyy-MM-dd").format(new Date())});

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
	public void createMetadata(UploadBucket bucket, String key[], String value[]) throws IOException {

		UploadBagitInfo metadata = bucket.getMetadata();
		String lines = metadata.add(key,value);
		Files.write( metadata.getPath().toPath() , lines.getBytes(), StandardOpenOption.APPEND);

	}
	
	
	
	@Override
	public byte[] readFile(UploadBucket bucket, String filename) throws IOException {
		File in = new File(bucket.getPayload(),filename);	
		return Files.readAllBytes(in.toPath());
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
