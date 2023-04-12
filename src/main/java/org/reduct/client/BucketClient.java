package org.reduct.client;

import org.reduct.common.exception.ReductException;
import org.reduct.model.bucket.BucketSettings;

public interface BucketClient {

   /**
    * Create a new bucket with the given name and settings.
    * NOTE: If, authentication is enabled on the server, an access token with full access must be provided
    * to create a new bucket.
    *
    * @param bucketName     The name of the bucket to create.
    * @param bucketSettings The settings for the bucket to create.
    *                       All settings are optional, and will be set to default values if not specified.
    * @throws ReductException If, unable to create the bucket. The instance of the exception holds the
    *                         status code (if available) to indicate the failure.
    *                         Status codes:
    *                         401 - Access token is invalid or was not provided.
    *                         403 - Access token does not have required permissions.
    *                         409 - Bucket with this name already exists.
    *                         422 - Invalid request.
    */
   void createBucket(String bucketName, BucketSettings bucketSettings) throws ReductException;
}
