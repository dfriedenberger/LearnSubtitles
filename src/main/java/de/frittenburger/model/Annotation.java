package de.frittenburger.model;

import java.util.ArrayList;
import java.util.List;

public class Annotation {

	private final List<Integer> indices = new ArrayList<Integer>();
	private final List<String> infos = new ArrayList<String>();
	
	public List<Integer> getIndices() {
		return indices;
	}
	
	public List<String> getInfos() {
		return infos;
	}
	
	@Override
	public String toString() {
		return "Annotation [indices=" + indices + ", infos=" + infos + "]";
	}
	

}
