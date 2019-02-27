package de.frittenburger.impl;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.interfaces.UserRepository;

public class UserRepositoryImpl implements UserRepository {

	private static final Logger logger = LogManager.getLogger(UserRepositoryImpl.class);

	private static UserRepository repository;
	private final File root;
	
	public UserRepositoryImpl(File root) throws IOException {
		this.root = root;
		if(!this.root.isDirectory())
			throw new IllegalArgumentException(root + " has to be a directory");

	}

	public static synchronized   UserRepository getInstance() {
		if(repository == null)
		{
			try {
				repository = new UserRepositoryImpl(new File("user"));
			} catch (IOException e) {
				logger.error(e);
			}
			
		}
		return repository;
	}
	
	private File getFileSecure(String userId, String key) throws IOException {
		
		try
		{
			String md5 = hash(userId);
			return new File(root,md5 +"/" + key+".json");
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		throw new IOException("Not able to create "+userId+" "+key);
	}
	
	
        
        
    @Override
	public void create(String userId) throws IOException {
    	try
		{
	    	String md5 = hash(userId);
			new File(root,md5).mkdir();	
			return;
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		throw new IOException("Not able to create "+userId);
	}

	@Override
	public void update(String userId, String key, Object obj) throws IOException {

		File f = getFileSecure(userId,key);
		new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(f, obj);
		
	}

	

	@Override
	public Map<String,String> read(String userId, String key) throws IOException {
		
		File f = getFileSecure(userId,key);
		if(!f.exists()) return null;
		return new ObjectMapper().readValue(f, new TypeReference<HashMap<String,String>>() {});
	
	}

	public static String hash(String username) throws GeneralSecurityException {
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(username.toLowerCase().getBytes());
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < thedigest.length; ++i) {
          sb.append(Integer.toHexString((thedigest[i] & 0xFF) | 0x100).substring(1,3));
        }
		return sb.toString();
	}

	

}
