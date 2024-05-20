package org.reduct.client;

import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.bucket.Bucket;
import org.reduct.model.bucket.BucketSettings;

public interface BucketClient {

   /**
    * Create a new bucket with the given name and settings.
    * NOTE: If, authentication is enabled on the server, an access token with full access must be provided
    * to create a new bucket.
    *
    * @param bucketName     The name of the bucket to create.
    * @param bucketSettings The settings for the bucket to create.
    *                       All settings are optional, setting which is not provided will be set to
    *                       default value.
    * @return The name of the bucket created. Never null.
    * @throws ReductException          If, unable to create the bucket. The instance of the exception holds
    *                                  the error message returned in the x-reduct-error header and the
    *                                  status code to indicate the failure.
    *                                  Some status codes:
    *                                  401 -> Access token is invalid or was not provided.
    *                                  403 -> Access token does not have required permissions.
    *                                  409 -> Bucket with this name already exists.
    *                                  422 -> Invalid request.
    *                                  500 -> Internal server error.
    * @throws ReductSDKException       If, any client side error occur.
    * @throws IllegalArgumentException If, the bucket name is null or empty.
    */
   String createBucket(String bucketName, BucketSettings bucketSettings)
           throws ReductException, ReductSDKException, IllegalArgumentException;

   Bucket getBucket(String bucketName) throws ReductException, ReductSDKException, IllegalArgumentException;
}
