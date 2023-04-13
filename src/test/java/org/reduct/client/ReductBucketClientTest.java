package org.reduct.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reduct.client.config.ServerProperties;
import org.reduct.client.util.BucketConstants;
import org.reduct.common.BucketURL;
import org.reduct.model.bucket.BucketSettings;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

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
      HttpRequest httpRequest = HttpRequest.newBuilder()
              .uri(uri)
              .header("Authorization", "Bearer %s".formatted(ACCESS_TOKEN))
              .POST(HttpRequest.BodyPublishers.ofString(BucketConstants.EMPTY_BUCKET_SETTINGS_BODY))
              .build();
      doReturn(200).when(mockHttpResponse).statusCode();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.discarding());
      String result = bucketClient.createBucket(BUCKET_NAME, BucketSettings.builder().build());

      assertEquals(BUCKET_NAME, result);
   }
}