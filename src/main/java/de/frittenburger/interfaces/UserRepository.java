package de.frittenburger.interfaces;

import java.io.IOException;
import java.util.Map;

public interface UserRepository {

	//CRUD
	
	void create(String userId) throws IOException;
	
	void update(String userId,String key,Object obj) throws IOException;

	Map<String, String> read(String userId, String key) throws IOException;
	
}
