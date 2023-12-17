package org.reduct.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
class ReductServerClientTest {

   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";

   @Spy
   private ServerProperties serverProperties = new ServerProperties(false, "localhost", 8383);
   @Mock
   private HttpClient httpClient;
   @InjectMocks
   @Spy
   private ReductServerClient reductServerClient;
   private String accessToken = "testToken";

   @BeforeEach
   public void setup() {
      doReturn(accessToken).when(reductServerClient).getToken();
   }

   @Test
   void getServerInfo_validDetails_returnServerInfo() throws IOException, InterruptedException {
      //Init
      HttpRequest httpRequest = createHttpRequest();
      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
      when(mockHttpResponse.statusCode()).thenReturn(200);
      when(mockHttpResponse.body()).thenReturn(ServerInfoConstants.SUCCESSFUL_SERVER_INFO_RESPONSE);
      //Act
      ServerInfo result = reductServerClient.getServerInfo();
      //Assert
      assertNotNull(result);
      assertEquals(sampleServerInfo(), result);
   }

   @Test
   void getServerInfo_serverReturnsMalformedJson_throwException() throws IOException, InterruptedException {
      //Init
      HttpRequest httpRequest = createHttpRequest();
      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
      when(mockHttpResponse.statusCode()).thenReturn(200);
      when(mockHttpResponse.body()).thenReturn("{{}");
      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
      //Act
      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getServerInfo());
      //Assert
      assertEquals("The server returned a malformed response.", result.getMessage());
   }

   @Test
   void getServerInfo_ioExceptionOccurs_throwException() throws IOException, InterruptedException {
      //Init
      HttpRequest httpRequest = createHttpRequest();
      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(IOException.class);
      //Act
      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getServerInfo());
      //Assert
      assertEquals("An error occurred while processing the request", result.getMessage());
   }

   @Test
   void getServerInfo_threadInterrupted_throwException() throws IOException, InterruptedException {
      //Init
      HttpRequest httpRequest = createHttpRequest();
      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(InterruptedException.class);
      //Act
      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getServerInfo());
      //Assert
      assertEquals("Thread has been interrupted while processing the request", result.getMessage());
   }

   @Test
   void getServerInfo_invalidToken_throwException() throws IOException, InterruptedException {
      //Init
      HttpRequest httpRequest = createHttpRequest();
      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
      Optional<String> errorHeader = Optional.of("Invalid token");
      HttpHeaders mockHttpHeaders = mock(HttpHeaders.class);

      when(mockHttpResponse.headers()).thenReturn(mockHttpHeaders);
      when(mockHttpHeaders.firstValue(REDUCT_ERROR_HEADER)).thenReturn(errorHeader);
      when(mockHttpResponse.statusCode()).thenReturn(401);
      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
      //Act
      ReductException result = assertThrows(ReductException.class, () -> reductServerClient.getServerInfo());
      //Assert
      assertEquals("Invalid token", result.getMessage());
      assertEquals(401, result.getStatusCode());
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