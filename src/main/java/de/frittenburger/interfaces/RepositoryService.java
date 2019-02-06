package de.frittenburger.interfaces;

import java.io.IOException;

import de.frittenburger.model.BucketMetadata;

public interface RepositoryService {


	BucketMetadata getBucketMetadata(String bucketId) throws IOException;

}
