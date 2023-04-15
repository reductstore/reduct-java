package org.reduct.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reduct.client.config.ServerProperties;
import org.reduct.client.util.ServerInfoConstants;
import org.reduct.common.ServerURL;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.server.ServerInfo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReductServerClientTest {

   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";

   private ServerProperties serverProperties;
   private HttpClient httpClient;
   private ReductServerClient reductServerClient;
   private String accessToken;

   @BeforeEach
   public void setup() {
      accessToken = "testToken";
      httpClient = mock(HttpClient.class);
      serverProperties = new ServerProperties(false, "localhost", 8383);
      reductServerClient = new ReductServerClient(serverProperties, httpClient, accessToken);
   }

   @Test
   void getServerInfo_validDetails_returnServerInfo() throws IOException, InterruptedException {
      HttpRequest httpRequest = createHttpRequest();
      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
      doReturn(200).when(mockHttpResponse).statusCode();
      doReturn(ServerInfoConstants.SUCCESSFUL_SERVER_INFO_RESPONSE).when(mockHttpResponse).body();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.ofString());
      ServerInfo result = reductServerClient.getServerInfo();

      assertNotNull(result);
      assertEquals(sampleServerInfo(), result);
   }

   @Test
   void getServerInfo_serverReturnsMalformedJson_throwException() throws IOException, InterruptedException {
      HttpRequest httpRequest = createHttpRequest();
      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
      doReturn(200).when(mockHttpResponse).statusCode();
      doReturn("{{}").when(mockHttpResponse).body();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.ofString());
      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getServerInfo());

      assertEquals("The server returned a malformed response.", result.getMessage());
   }

   @Test
   void getServerInfo_ioExceptionOccurs_throwException() throws IOException, InterruptedException {
      HttpRequest httpRequest = createHttpRequest();
      doThrow(IOException.class).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.ofString());
      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getServerInfo());

      assertEquals("An error occurred while processing the request", result.getMessage());
   }

   @Test
   void getServerInfo_threadInterrupted_throwException() throws IOException, InterruptedException {
      HttpRequest httpRequest = createHttpRequest();
      doThrow(InterruptedException.class).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.ofString());
      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getServerInfo());

      assertEquals("Thread has been interrupted while processing the request", result.getMessage());
   }

   @Test
   void getServerInfo_invalidToken_throwException() throws IOException, InterruptedException {
      HttpRequest httpRequest = createHttpRequest();
      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
      Optional<String> errorHeader = Optional.of("Invalid token");
      HttpHeaders mockHttpHeaders = mock(HttpHeaders.class);

      doReturn(mockHttpHeaders).when(mockHttpResponse).headers();
      doReturn(errorHeader).when(mockHttpHeaders).firstValue(REDUCT_ERROR_HEADER);
      doReturn(401).when(mockHttpResponse).statusCode();
      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.ofString());
      ReductException result = assertThrows(ReductException.class, () -> reductServerClient.getServerInfo());

      assertEquals("Invalid token", result.getMessage());
      assertEquals(401, result.getStatusCode());
   }

   @Test
   void constructClient_serverPropertiesIsNull_throwException() {
      IllegalArgumentException result = assertThrows(IllegalArgumentException.class,
              () -> new ReductServerClient(null, httpClient, accessToken));

      assertEquals("ServerProperties cannot be null.", result.getMessage());
   }

   private HttpRequest createHttpRequest() {
      return HttpRequest.newBuilder()
              .uri(URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), ServerURL.SERVER_INFO.getUrl())))
              .GET()
              .header("Authorization", "Bearer %s".formatted(accessToken))
              .build();
   }

   private ServerInfo sampleServerInfo() throws JsonProcessingException {
      return new ObjectMapper().readValue(ServerInfoConstants.SUCCESSFUL_SERVER_INFO_RESPONSE, ServerInfo.class);
   }
}