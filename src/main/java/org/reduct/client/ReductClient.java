package org.reduct.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.reduct.client.config.ServerProperties;
import org.reduct.common.ServerURL;
import org.reduct.common.TokenURL;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.bucket.Bucket;
import org.reduct.model.bucket.Buckets;
import org.reduct.model.server.ServerInfo;
import org.reduct.model.token.AccessToken;
import org.reduct.model.token.AccessTokens;
import org.reduct.model.token.TokenPermissions;
import org.reduct.utils.JsonUtils;
import org.reduct.utils.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.reduct.utils.Strings.isNotBlank;

/**
 * Base class for all clients.
 */
@RequiredArgsConstructor
@Getter
public class ReductClient {
   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";
   private final ServerProperties serverProperties;
   private final HttpClient httpClient;
   @Getter(AccessLevel.PRIVATE)
   private final String token;
   private final ObjectMapper objectMapper = new ObjectMapper();

   public <T> HttpResponse<T> send(HttpRequest.Builder builder, HttpResponse.BodyHandler<T> bodyHandler) {
      try {
         if(isNotBlank(token)) {
            builder.headers("Authorization", "Bearer %s".formatted(token));
         }
         HttpResponse<T> httpResponse = httpClient.send(builder.build(), bodyHandler);
          if (httpResponse.statusCode() == 200) {
              return httpResponse;
          }
          throw new ReductException(httpResponse.headers().firstValue(REDUCT_ERROR_HEADER).orElse("Unsuccessful request"), httpResponse.statusCode());
      }
      catch (IOException e) {
         throw new ReductSDKException("An error occurred while processing the request", e);
      }
      catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new ReductSDKException("Thread has been interrupted while processing the request", e);
      }
   }

   public Bucket getBucket(String name) throws ReductException, ReductSDKException, IllegalArgumentException {
      return new Bucket(name, this).read();
   }

   public Bucket createBucket(String name) throws ReductException, ReductSDKException, IllegalArgumentException {
      return new Bucket(name, this).write();
   }

   /**
    * Get statistical information about the storage. You can use this method to get stats of the storage and check its version.
    * Attempts to retrieve the server information. Such as, version, bucket count, usage, uptime, oldest record,
    * latest record, and default settings.
    *
    * @return {@link ServerInfo} object if the request is successful.
    * @throws ReductException    if the request fails. The instance of the exception holds
    *                            the error message returned in the x-reduct-error header and the
    *                            status code to indicate the failure.
    * @throws ReductSDKException If, any client side error occurs.
    */
   public ServerInfo info() throws ReductException, ReductSDKException {
      URI uri = URI.create("%s/%s".formatted(getServerProperties().getBaseUrl(), ServerURL.SERVER_INFO.getUrl()));
      HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri).GET();
      HttpResponse<String> httpResponse = send(builder, HttpResponse.BodyHandlers.ofString());
      return JsonUtils.parseObject(httpResponse.body(), ServerInfo.class);
   }

   /**
    * Get a list of the buckets with their stats
    *
    * @return Collection of {@link org.reduct.model.bucket.Bucket}
    * @throws ReductException    if the request fails. The instance of the exception holds
    *                            the error message returned in the x-reduct-error header and the
    *                            status code to indicate the failure.
    * @throws ReductSDKException If, any client side error occurs.
    */
   public Buckets list() throws ReductException, ReductSDKException {
      URI uri = URI.create("%s/%s".formatted(getServerProperties().getBaseUrl(), ServerURL.LIST.getUrl()));
      HttpRequest.Builder builder = HttpRequest.newBuilder()
              .uri(uri)
              .GET();
      HttpResponse<String> httpResponse = send(builder, HttpResponse.BodyHandlers.ofString());
      return JsonUtils.parseObject(httpResponse.body(), Buckets.class);
   }

   /**
    * Check if the storage engine is working
    * @return true if the server is working
    * @throws ReductException    if the request fails. The instance of the exception holds
    *                            the error message returned in the x-reduct-error header and the
    *                            status code to indicate the failure.
    * @throws ReductSDKException If, any client side error occurs.
    */
   public Boolean isAlive() throws ReductException, ReductSDKException {
      URI uri = URI.create("%s/%s".formatted(getServerProperties().getBaseUrl(), ServerURL.ALIVE.getUrl()));
      HttpRequest.Builder builder = HttpRequest.newBuilder()
              .uri(uri)
              .method("HEAD", HttpRequest.BodyPublishers.noBody());
      HttpResponse<String> httpResponse = send(builder, HttpResponse.BodyHandlers.ofString());
      return HttpStatus.OK.getCode().equals(httpResponse.statusCode());
   }

   /**
    * The method returns a list of tokens with names and creation dates. To use this method, you need an access token with full access.
    * @return AccessTokens
    */
   public AccessTokens tokens() throws ReductException, ReductSDKException {
      URI uri = URI.create("%s/%s".formatted(getServerProperties().getBaseUrl(), TokenURL.GET_TOKENS.getUrl()));
      HttpRequest.Builder builder = HttpRequest.newBuilder()
              .uri(uri)
              .GET();
      HttpResponse<String> httpResponse = send(builder, HttpResponse.BodyHandlers.ofString());
      return JsonUtils.parseObject(httpResponse.body(), AccessTokens.class);
   }

   /**
    * Attempts to create a new access token with the given permissions.
    *
    * @param tokenName   The name of the token to create.
    * @param permissions The permissions to give to the token. Such as, whether it has full access to the server,
    *                    or only read access to some buckets, or write access to some buckets.
    * @return {@link AccessToken} object that holds the token and the creation date.
    * @throws ReductException          If the request fails, for example the token already exists, or
    *                                  any of the buckets listed does not exist on the server.
    *                                  The instance of the exception holds the error message returned in
    *                                  the x-reduct-error header and the status code to indicate the failure.
    * @throws ReductSDKException       If, any client side error occurs.
    * @throws IllegalArgumentException If the token name is null or blank, or if the
    *                                  {@link org.reduct.model.token.TokenPermissions} object is null.
    */
   public AccessToken createToken(String tokenName, TokenPermissions permissions) throws ReductException, ReductSDKException, IllegalArgumentException {
      if (tokenName == null || tokenName.isBlank()) {
         throw new IllegalArgumentException("Token name must not be null or blank");
      }
      if (permissions == null) {
         throw new IllegalArgumentException("Permissions must not be null");
      }
      String createTokenPath = TokenURL.CREATE_TOKEN.getUrl().formatted(tokenName);
      String createTokenBody = JsonUtils.serialize(permissions);

      HttpRequest.Builder builder = HttpRequest.newBuilder()
              .uri(URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createTokenPath)))
              .POST(HttpRequest.BodyPublishers.ofString(createTokenBody));
      HttpResponse<String> response = send(builder, HttpResponse.BodyHandlers.ofString());
      return JsonUtils.parseObject(response.body(), AccessToken.class);
   }
}
