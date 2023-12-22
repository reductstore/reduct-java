package org.reduct.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import org.reduct.client.config.ServerProperties;
import org.reduct.common.TokenURL;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.token.AccessToken;
import org.reduct.model.token.AccessTokens;
import org.reduct.model.token.TokenPermissions;
import org.reduct.utils.JsonUtils;
import org.reduct.utils.http.Method;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.reduct.utils.Strings.isNotBlank;

public class ReductTokenClient extends ReductClient implements TokenClient {

   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";
   @Getter(value = AccessLevel.PACKAGE)
   protected final ServerProperties serverProperties;
   @Getter(value = AccessLevel.PACKAGE)
   protected final HttpClient httpClient;
   protected final ObjectMapper objectMapper;
   @Getter(value = AccessLevel.PACKAGE)
   protected final String token;

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
      this.httpClient = client;
      this.serverProperties = serverProperties;
      this.objectMapper = new ObjectMapper();
      this.token = accessToken;
   }

   @Override
   public AccessToken createToken(String tokenName, TokenPermissions permissions)
           throws ReductException, ReductSDKException, IllegalArgumentException {
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
      if (response.statusCode() == 200) {
         return JsonUtils.parseObject(response.body(), AccessToken.class);
      } else {
         String errorMessage = response.headers()
                 .firstValue(REDUCT_ERROR_HEADER)
                 .orElse("Failed to create token");
         throw new ReductException(errorMessage, response.statusCode());
      }
   }

   @Override
   public AccessTokens getTokens() throws ReductException, ReductSDKException {
      HttpResponse<String> httpResponse = sendRequest(TokenURL.GET_TOKENS.getUrl(), Method.GET);
      return JsonUtils.parseObject(httpResponse.body(), AccessTokens.class);
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
         throw new ReductSDKException("Failed to serialize the token permissions.", e);
      }
   }

   HttpResponse<String> sendRequest(HttpRequest getRequest) {
      try {
         return httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
      } catch (IOException e) {
         throw new ReductSDKException("An error occurred while processing the request", e);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new ReductSDKException("Thread has been interrupted while processing the request", e);
      }
   }
}
