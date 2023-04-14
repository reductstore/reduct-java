package org.reduct.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reduct.client.config.ServerProperties;
import org.reduct.common.BucketURL;
import org.reduct.common.exception.ReductException;
import org.reduct.model.bucket.BucketSettings;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ReductBucketClient extends ReductClient implements BucketClient {

   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";

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
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
   }

   @Override
   public String createBucket(String bucketName, BucketSettings bucketSettings) {
      if (bucketName == null || bucketName.isBlank()) {
         throw new IllegalArgumentException("Bucket name cannot be null or empty");
      }
      String requestBody = serializeSettingsOrEmptyJson(bucketSettings);
      HttpRequest httpRequest = createHttpRequest(bucketName, requestBody);
      HttpResponse<Void> httpResponse = executeHttpRequest(httpRequest);
      if (httpResponse.statusCode() == 200) {
         return bucketName;
      } else {
         String errorMessage = httpResponse.headers()
                 .firstValue(REDUCT_ERROR_HEADER)
                 .orElse("Failed to create bucket");
         throw new ReductException(errorMessage, httpResponse.statusCode());
      }
   }

   private String serializeSettingsOrEmptyJson(BucketSettings bucketSettings) {
      if (bucketSettings == null) {
         return "{}";
      }
      try {
         return objectMapper.writeValueAsString(bucketSettings);
      } catch (JsonProcessingException e) {
         throw new ReductException("Failed to serialize bucket settings", e);
      }
   }

   private HttpRequest createHttpRequest(String bucketName, String requestBody) {
      return HttpRequest.newBuilder()
              .uri(constructCreateBucketUri(bucketName))
              .POST(HttpRequest.BodyPublishers.ofString(requestBody))
              .header("Authorization", "Bearer %s".formatted(token))
              .build();
   }

   private HttpResponse<Void> executeHttpRequest(HttpRequest httpRequest) {
      try {
         return httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
      } catch (IOException e) {
         throw new ReductException("An error occurred while processing the request", e);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new ReductException("Thread has been interrupted while processing the request", e);
      }
   }

   private URI constructCreateBucketUri(String bucketName) {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(bucketName);
      return URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
   }
}
