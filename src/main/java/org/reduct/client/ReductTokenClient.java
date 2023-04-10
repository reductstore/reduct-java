package org.reduct.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reduct.client.config.ServerProperties;
import org.reduct.common.TokenURL;
import org.reduct.common.exception.ReductException;
import org.reduct.model.token.AccessToken;
import org.reduct.model.token.TokenPermissions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ReductTokenClient implements TokenClient {

   private final ServerProperties properties;
   private final HttpClient httpClient;
   private final ObjectMapper objectMapper;
   private final String token;

   public ReductTokenClient(ServerProperties serverProperties, String accessToken) {
      this(serverProperties, HttpClient.newHttpClient(), accessToken);
   }

   ReductTokenClient(ServerProperties serverProperties, HttpClient client, String accessToken) {
      properties = serverProperties;
      httpClient = client;
      token = accessToken;
      objectMapper = new ObjectMapper();
   }

   @Override
   public AccessToken createToken(String tokenName, TokenPermissions permissions) throws ReductException {
      URI createTokenUri = constructCreateTokenUri(tokenName);
      String createTokenBody = serializeCreateTokenBody(permissions);
      HttpRequest createTokenRequest = constructCreateTokenRequest(createTokenUri, createTokenBody);
      HttpResponse<String> response = sendRequest(createTokenRequest);
      if (response.statusCode() == 200) {
         return parseAccessToken(response.body());
      } else {
         throw new ReductException("The server returned an unexpected response. Please try again later.",
                 response.statusCode());
      }
   }

   private HttpResponse<String> sendRequest(HttpRequest createTokenRequest) {
      try {
         return httpClient.send(createTokenRequest, HttpResponse.BodyHandlers.ofString());
      } catch (IOException e) {
         throw new ReductException("An error occurred while processing the request", e);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new ReductException("Thread has been interrupted while processing the request", e);
      }
   }

   private HttpRequest constructCreateTokenRequest(URI createTokenUri, String createTokenBody) {
      return HttpRequest.newBuilder()
              .uri(createTokenUri)
              .POST(HttpRequest.BodyPublishers.ofString(createTokenBody))
              .header("Authorization", "Bearer %s".formatted(token))
              .build();
   }

   private URI constructCreateTokenUri(String tokenName) {
      String createTokenPath = TokenURL.CREATE_TOKEN.getUrl().formatted(tokenName);
      return URI.create("%s/%s".formatted(properties.getBaseUrl(), createTokenPath));
   }

   private String serializeCreateTokenBody(TokenPermissions permissions) {
      try {
         return objectMapper.writeValueAsString(permissions);
      } catch (JsonProcessingException e) {
         throw new ReductException("Failed to serialize the token permissions.", e);
      }
   }

   private AccessToken parseAccessToken(String body) {
      try {
         return objectMapper.readValue(body, AccessToken.class);
      } catch (JsonProcessingException e) {
         throw new ReductException("Failed to parse the access token.", e);
      }
   }
}
