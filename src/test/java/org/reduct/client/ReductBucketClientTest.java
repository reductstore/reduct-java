package org.reduct.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reduct.client.config.ServerProperties;
import org.reduct.client.util.BucketExamples;
import org.reduct.common.BucketURL;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.bucket.BucketSettings;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReductBucketClientTest {

   private static final String BUCKET_NAME = "test-bucket";
   private static final String ACCESS_TOKEN = "test-access-token";
   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";

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
      HttpRequest httpRequest = createHttpRequest(uri, BucketExamples.EMPTY_BUCKET_SETTINGS_BODY);
      doReturn(200).when(mockHttpResponse).statusCode();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      String result = bucketClient.createBucket(BUCKET_NAME, BucketSettings.builder().build());

      assertEquals(BUCKET_NAME, result);
   }

   @Test
   void createBucket_settingsProvided_bucketCreated() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketExamples.SAMPLE_BUCKET_SETTINGS_BODY);
      doReturn(200).when(mockHttpResponse).statusCode();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      String result = bucketClient.createBucket(BUCKET_NAME, BucketSettings.builder().build());

      assertEquals(BUCKET_NAME, result);
   }

   @Test
   void createBucket_settingsNull_bucketCreated() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketExamples.SAMPLE_BUCKET_SETTINGS_BODY);
      doReturn(200).when(mockHttpResponse).statusCode();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      String result = bucketClient.createBucket(BUCKET_NAME, null);

      assertEquals(BUCKET_NAME, result);
   }

   @Test
   void createBucket_ioException_throwsException() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketExamples.SAMPLE_BUCKET_SETTINGS_BODY);
      doThrow(IOException.class).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      ReductSDKException result = assertThrows(ReductSDKException.class,
              () -> bucketClient.createBucket(BUCKET_NAME, null));

      assertEquals("An error occurred while processing the request", result.getMessage());
   }

   @Test
   void createBucket_interruptedException_throwsException() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketExamples.SAMPLE_BUCKET_SETTINGS_BODY);
      doThrow(InterruptedException.class).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      ReductSDKException result = assertThrows(ReductSDKException.class,
              () -> bucketClient.createBucket(BUCKET_NAME, null));

      assertEquals("Thread has been interrupted while processing the request", result.getMessage());
   }

   @Test
   void createBucket_bucketAlreadyExists_throwsException() throws IOException, InterruptedException {
      String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(BUCKET_NAME);
      URI uri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
      HttpRequest httpRequest = createHttpRequest(uri, BucketExamples.SAMPLE_BUCKET_SETTINGS_BODY);
      Optional<String> errorHeader = Optional.of("Bucket 'test-bucket' already exists");
      HttpHeaders mockHttpHeaders = mock(HttpHeaders.class);

      doReturn(mockHttpHeaders).when(mockHttpResponse).headers();
      doReturn(errorHeader).when(mockHttpHeaders).firstValue(REDUCT_ERROR_HEADER);
      doReturn(409).when(mockHttpResponse).statusCode();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());

      ReductException result = assertThrows(ReductException.class,
              () -> bucketClient.createBucket(BUCKET_NAME, BucketSettings.builder().build()));

      assertEquals("Bucket 'test-bucket' already exists", result.getMessage());
      assertEquals(409, result.getStatusCode());
   }

   @Test
   void createBucket_bucketNameNull_throwsException() {
      IllegalArgumentException result = assertThrows(IllegalArgumentException.class,
              () -> bucketClient.createBucket(null, BucketSettings.builder().build()));
      assertEquals("Bucket name cannot be null or empty", result.getMessage());
   }

   @Test
   void createBucket_bucketNameEmpty_throwsException() {
      IllegalArgumentException result = assertThrows(IllegalArgumentException.class,
              () -> bucketClient.createBucket("", BucketSettings.builder().build()));
      assertEquals("Bucket name cannot be null or empty", result.getMessage());
   }

   private static HttpRequest createHttpRequest(URI uri, String requestBody) {
      return HttpRequest.newBuilder()
              .uri(uri)
              .header("Authorization", "Bearer %s".formatted(ACCESS_TOKEN))
              .POST(HttpRequest.BodyPublishers.ofString(requestBody))
              .build();
   }
}