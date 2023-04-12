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

public class ReductTokenClient extends ReductClient implements TokenClient {

   /**
    * Constructs a new ReductTokenClient with the given properties.
    * NOTE: Client created without access token will not be able to interact with the server if,
    * authentication is enabled on the server.
    *
    * @param serverProperties The properties, such as host and port
    */
   public ReductTokenClient(ServerProperties serverProperties) {
      this(serverProperties, null);
   }

   /**
    * Constructs a new ReductTokenClient with the given properties and the given access token.
    *
    * @param serverProperties The properties, such as host and port
    * @param accessToken      The access token to use for authentication
    */
   public ReductTokenClient(ServerProperties serverProperties, String accessToken) {
      this(serverProperties, HttpClient.newHttpClient(), accessToken);
   }

   ReductTokenClient(ServerProperties serverProperties, HttpClient client, String accessToken) {
      super(serverProperties, client, new ObjectMapper(), accessToken);
   }

   @Override
   public AccessToken createToken(String tokenName, TokenPermissions permissions) throws ReductException {
      if (tokenName == null || tokenName.isBlank()) {
         throw new IllegalArgumentException("Token name must not be null or blank");
      }
      if (permissions == null) {
         throw new IllegalArgumentException("Permissions must not be null");
      }
      URI createTokenUri = constructCreateTokenUri(tokenName);
      String createTokenBody = serializeCreateTokenBody(permissions);
      HttpRequest createTokenRequest = constructCreateTokenRequest(createTokenUri, createTokenBody);
      HttpResponse<String> response = sendRequest(createTokenRequest);
      return handleResponse(response);
   }

   private AccessToken handleResponse(HttpResponse<String> response) {
      return switch (response.statusCode()) {
         case 200 -> parseAccessToken(response.body());
         case 401 -> throw new ReductException("The access token is invalid.", response.statusCode());
         case 403 -> throw new ReductException("The access token does not have the required permissions.",
                 response.statusCode());
         case 409 -> throw new ReductException("A token already exists with this name.", response.statusCode());
         case 422 -> throw new ReductException("One of the bucket names provided does not exist on the server.",
                 response.statusCode());
         default -> throw new ReductException("The server returned an unexpected response. Please try again later.",
                 response.statusCode());
      };
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
      return URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createTokenPath));
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
         throw new ReductException("The server returned a malformed response.", e);
      }
   }
}
