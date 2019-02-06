package de.frittenburger.srt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

public class SrtMergeWriter {

	
	private final PrintWriter out;


	public SrtMergeWriter(File file) throws IOException
	{
		this(new FileOutputStream(file));
	 
	}
	
	public SrtMergeWriter(OutputStream os) throws IOException {
		this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter( os, "UTF-8")));	
	}


	public void write(SrtCluster cluster)
	{
		Map<String,Integer> counter = cluster.getCounter();
		
		if(counter.size() != 2)
		{
			System.out.println(counter);
			out.println("#error");
		}
		
		if(cluster.size() > 16) {
			System.out.println(counter);
			out.println("#check" + cluster.size());
		}
		
		//Write Record
		out.println("[ ");
		for (SrtRecord r1 : cluster) {
			out.println(" " + r1);
		}
		out.println("]");
		
	}

	public void close() {
		this.out.close();
	}
	
}
