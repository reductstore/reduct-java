package org.reduct.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reduct.client.config.ServerProperties;
import org.reduct.client.util.BucketConstants;
import org.reduct.common.BucketURL;
import org.reduct.common.exception.ReductException;
import org.reduct.model.bucket.BucketSettings;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReductBucketClientTest {

   private static final String BUCKET_NAME = "test-bucket";
   private static final String ACCESS_TOKEN = "test-access-token";

   private ServerProperties serverProperties;
   private ReductBucketClient bucketClient;
   private HttpClient httpClient;
   private HttpResponse<Void> mockHttpResponse;

   @BeforeEach
   public void init() {
      serverProperties = new ServerProperties(false, "127.0.0.1", 8383);
      httpClient = mock(HttpClient.class);
      bucketClient = new ReductBucketClient(serverProperties, httpClient, ACCESS_TOKEN);
      mockHttpResponse = mock(HttpResponse.class);
   }

   @Test
   void createBucket_settingsNotProvided_bucketCreated() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketConstants.EMPTY_BUCKET_SETTINGS_BODY);
      doReturn(200).when(mockHttpResponse).statusCode();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      String result = bucketClient.createBucket(BUCKET_NAME, BucketSettings.builder().build());

      assertEquals(BUCKET_NAME, result);
   }

   @Test
   void createBucket_settingsProvided_bucketCreated() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketConstants.SAMPLE_BUCKET_SETTINGS_BODY);
      doReturn(200).when(mockHttpResponse).statusCode();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      String result = bucketClient.createBucket(BUCKET_NAME, BucketSettings.builder().build());

      assertEquals(BUCKET_NAME, result);
   }

   @Test
   void createBucket_settingsNull_bucketCreated() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketConstants.SAMPLE_BUCKET_SETTINGS_BODY);
      doReturn(200).when(mockHttpResponse).statusCode();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      String result = bucketClient.createBucket(BUCKET_NAME, null);

      assertEquals(BUCKET_NAME, result);
   }

   @Test
   void createBucket_ioException_throwsException() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketConstants.SAMPLE_BUCKET_SETTINGS_BODY);
      doThrow(IOException.class).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      ReductException result = assertThrows(ReductException.class,
              () -> bucketClient.createBucket(BUCKET_NAME, null));

      assertEquals("An error occurred while processing the request", result.getMessage());
   }

   @Test
   void createBucket_interruptedException_throwsException() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketConstants.SAMPLE_BUCKET_SETTINGS_BODY);
      doThrow(InterruptedException.class).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      ReductException result = assertThrows(ReductException.class,
              () -> bucketClient.createBucket(BUCKET_NAME, null));

      assertEquals("Thread has been interrupted while processing the request", result.getMessage());
   }

   @Test
   void createBucket_serverReturnsUnauthorized_throwsException() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketConstants.SAMPLE_BUCKET_SETTINGS_BODY);
      doReturn(401).when(mockHttpResponse).statusCode();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      ReductException result = assertThrows(ReductException.class,
              () -> bucketClient.createBucket(BUCKET_NAME, BucketSettings.builder().build()));

      assertEquals("The access token is invalid", result.getMessage());
      assertEquals(401, result.getStatusCode());
   }

   @Test
   void createBucket_serverReturnsForbidden_throwsException() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketConstants.SAMPLE_BUCKET_SETTINGS_BODY);
      doReturn(403).when(mockHttpResponse).statusCode();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      ReductException result = assertThrows(ReductException.class,
              () -> bucketClient.createBucket(BUCKET_NAME, BucketSettings.builder().build()));

      assertEquals("The access token does not have required permissions", result.getMessage());
      assertEquals(403, result.getStatusCode());
   }

   private static HttpRequest createHttpRequest(URI uri, String requestBody) {
      return HttpRequest.newBuilder()
              .uri(uri)
              .header("Authorization", "Bearer %s".formatted(ACCESS_TOKEN))
              .POST(HttpRequest.BodyPublishers.ofString(requestBody))
              .build();
   }
}