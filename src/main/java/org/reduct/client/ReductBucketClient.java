package org.reduct.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reduct.client.config.ServerProperties;
import org.reduct.model.bucket.BucketSettings;

import java.net.http.HttpClient;

public class ReductBucketClient extends ReductClient implements BucketClient {

   /**
    * Create a new bucket client with the given server properties.
    * NOTE: Client created without access token will not be able to interact with the server if,
    * authentication is enabled on the server.
    *
    * @param serverProperties The server properties to use.
    */
   public ReductBucketClient(ServerProperties serverProperties) {
      this(serverProperties, null);
   }

   /**
    * Create a new bucket client with the given server properties and access token.
    * NOTE: If, authentication is enabled on the server, an access token with full access must be provided
    * to create a new bucket.
    *
    * @param serverProperties The server properties to use.
    * @param accessToken      The access token to use.
    */
   public ReductBucketClient(ServerProperties serverProperties, String accessToken) {
      this(serverProperties, HttpClient.newHttpClient(), accessToken);
   }

   ReductBucketClient(ServerProperties serverProperties, HttpClient httpClient, String accessToken) {
      super(serverProperties, httpClient, new ObjectMapper(), accessToken);
   }

   @Override
   public void createBucket(String bucketName, BucketSettings bucketSettings) {

   }
}
