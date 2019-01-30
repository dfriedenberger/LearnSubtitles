package de.frittenburger.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.controller.PageController;
import de.frittenburger.interfaces.UnzipService;

public class UnzipServiceImpl implements UnzipService {

	private static final Logger logger = LogManager.getLogger(PageController.class);

	@Override
	public File extract(File zipFile, File path, String extensionFilter) {

		if(!zipFile.isFile())
			throw new IllegalArgumentException("zipFile");
		
		if(!path.isDirectory())
			throw new IllegalArgumentException("path");
		

		 byte[] buffer = new byte[1024];
		 File newFile = null;
	     try{
	    		
	    	
	    		
	    	//get the zip file content
	    	ZipInputStream zis = 
	    		new ZipInputStream(new FileInputStream(zipFile));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();
	    		
	    	while(ze!=null){
	    			
	    	   String fileName = ze.getName();
	    	   if(fileName.endsWith(extensionFilter))
	    	   {
	    		   
		           newFile = new File(path,new File(fileName).getName());
		                
		           logger.info("unzip {}" , newFile.getAbsoluteFile());
		              
		            FileOutputStream fos = new FileOutputStream(newFile);             
		
		            int len;
		            while ((len = zis.read(buffer)) > 0) {
		       		fos.write(buffer, 0, len);
		            }
		        		
		            fos.close();   
	    	   }
	            ze = zis.getNextEntry();
	    	}
	    	
	        zis.closeEntry();
	    	zis.close();
	    		
	    		
	    }catch(IOException ex){
	       logger.error(ex);
	    }
		
		return newFile;
	}

}
