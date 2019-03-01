package de.frittenburger.srt;

import java.io.IOException;
import java.util.List;

public interface SrtMerger {
	
	List<SrtCluster> merge(SrtReader srtreader1, SrtReader srtreader2) throws IOException;

}
