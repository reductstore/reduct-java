package org.reduct.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reduct.client.config.ServerProperties;
import org.reduct.client.util.TokenConstants;
import org.reduct.common.TokenURL;
import org.reduct.model.token.AccessToken;
import org.reduct.model.token.TokenPermissions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ReductTokenClientTest {

   private static final String TOKEN_NAME = "test";

   private TokenClient reductTokenClient;
   private ServerProperties serverProperties;
   private HttpClient httpClient;
   private String accessToken;

   @BeforeEach
   public void setup() {
      accessToken = "testToken";
      httpClient = mock(HttpClient.class);
      serverProperties = new ServerProperties(false, "localhost", 8383);
      reductTokenClient = new ReductTokenClient(serverProperties, httpClient, accessToken);
   }

   @Test
   void createToken_validDetails_returnToken() throws IOException, InterruptedException {
      String createTokenPath = TokenURL.CREATE_TOKEN.getUrl().formatted(TOKEN_NAME);
      URI createTokenUri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createTokenPath));
      HttpRequest httpRequest = HttpRequest.newBuilder()
              .uri(createTokenUri)
              .POST(HttpRequest.BodyPublishers.ofString(TokenConstants.CREATE_TOKEN_REQUEST_BODY))
              .header("Authorization", "Bearer %s".formatted(accessToken))
              .build();
      HttpResponse<String> httpResponse = mock(HttpResponse.class);
      doReturn(200).when(httpResponse).statusCode();
      doReturn(TokenConstants.CREATE_TOKEN_SUCCESSFUL_RESPONSE).when(httpResponse).body();
      doReturn(httpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.ofString());

      AccessToken token = reductTokenClient.createToken(TOKEN_NAME,
              TokenPermissions.of(true, List.of("test-bucket"), List.of("test-bucket")));

      assertEquals(TokenConstants.EXPECTED_TOKEN_VALUE, token.value());
      assertEquals(TokenConstants.EXPECTED_CREATION_DATE, token.createdAt());
   }
}