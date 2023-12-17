package org.reduct.client;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.reduct.client.config.ServerProperties;
import org.reduct.common.ServerURL;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.bucket.Buckets;
import org.reduct.model.server.ServerInfo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.reduct.utils.Strings.isNotBlank;

@Getter
public class ReductServerClient implements ServerClient {

   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";
   protected final ServerProperties serverProperties;
   protected final HttpClient httpClient;
   protected final ObjectMapper objectMapper;
   protected final String token;

   /**
    * Constructs a new ReductServerClient with the given properties.
    * NOTE: Client created without access token will not be able to interact with the server if,
    * authentication is enabled on the server.
    *
    * @param serverProperties The properties, such as host and port
    */
   public ReductServerClient(ServerProperties serverProperties) {
      this(serverProperties, null);
   }

   /**
    * Constructs a new ReductServerClient with the given properties and the given access token.
    *
    * @param serverProperties The properties, such as host and port
    * @param accessToken      The access token to use for authentication
    */
   public ReductServerClient(ServerProperties serverProperties, String accessToken) {
      this(serverProperties, HttpClient.newHttpClient(), accessToken);
   }

   ReductServerClient(ServerProperties serverProperties, HttpClient httpClient, String accessToken) {
      this.httpClient = httpClient;
      this.serverProperties = serverProperties;
      this.objectMapper = new ObjectMapper();
      this.token = accessToken;
   }

   @Override
   public ServerInfo getServerInfo() throws ReductException, ReductSDKException {
      URI serverInfoUri = URI.create("%s/%s".formatted(getServerProperties().getBaseUrl(), ServerURL.SERVER_INFO.getUrl()));
      HttpRequest.Builder builder = HttpRequest.newBuilder()
              .GET()
              .uri(serverInfoUri);
      if(isNotBlank(getToken())) {
         builder.header("Authorization", "Bearer %s".formatted(getToken()));
      }
      try {
         HttpResponse<String> httpResponse = getHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString());
         if (httpResponse.statusCode() == 200) {
            return parseObject(httpResponse.body(), ServerInfo.class);

         } else {
            String errorMessage = httpResponse.headers()
                    .firstValue(REDUCT_ERROR_HEADER)
                    .orElse("Failed to create bucket");
            throw new ReductException(errorMessage, httpResponse.statusCode());
         }
      }
      catch (IOException e) {
         throw new ReductSDKException("An error occurred while processing the request", e);
      }
      catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new ReductSDKException("Thread has been interrupted while processing the request", e);
      }
   }

   @Override
   public Buckets getList() throws ReductException, ReductSDKException {
      URI serverInfoUri = URI.create("%s/%s".formatted(getServerProperties().getBaseUrl(), ServerURL.LIST.getUrl()));
      HttpRequest.Builder builder = HttpRequest.newBuilder()
              .GET()
              .uri(serverInfoUri);
      if(isNotBlank(getToken())) {
         builder.header("Authorization", "Bearer %s".formatted(getToken()));
      }
      try {
         HttpResponse<String> httpResponse = getHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString());
         if (httpResponse.statusCode() == 200) {
             return parseObject(httpResponse.body(), Buckets.class);
         } else {
            String errorMessage = httpResponse.headers()
                    .firstValue(REDUCT_ERROR_HEADER)
                    .orElse("Failed to create bucket");
            throw new ReductException(errorMessage, httpResponse.statusCode());
         }
      }
      catch (IOException e) {
         throw new ReductSDKException("An error occurred while processing the request", e);
      }
      catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new ReductSDKException("Thread has been interrupted while processing the request", e);
      }
   }
   private <T> T parseObject(String serverInfo, Class<T> tClass) {
      try {
         return objectMapper.readValue(serverInfo, tClass);
      } catch (JacksonException e) {
         throw new ReductSDKException("The server returned a malformed response.");
      }
   }
   private <T> List<T> parseObjectAsList(String serverInfo, Class<T> tClass) {
      try {
         return objectMapper.readValue(serverInfo, objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
      } catch (JacksonException e) {
         throw new ReductSDKException("The server returned a malformed response.");
      }
   }
}
