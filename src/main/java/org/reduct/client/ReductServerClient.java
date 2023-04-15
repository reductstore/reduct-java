package org.reduct.client;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reduct.client.config.ServerProperties;
import org.reduct.common.ServerURL;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.server.ServerInfo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ReductServerClient extends ReductClient implements ServerClient {

   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";

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

   ReductServerClient(ServerProperties serverProperties, HttpClient client, String accessToken) {
      super(serverProperties, client, new ObjectMapper(), accessToken);
   }

   @Override
   public ServerInfo getServerInfo() throws ReductException, ReductSDKException {
      URI serverInfoUri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), ServerURL.SERVER_INFO.getUrl()));
      HttpRequest getRequest = HttpRequest.newBuilder()
              .GET()
              .uri(serverInfoUri)
              .header("Authorization", "Bearer %s".formatted(token))
              .build();
      HttpResponse<String> httpResponse = sendRequest(getRequest);
      if (httpResponse.statusCode() == 200) {
         return parseServerInfo(httpResponse.body());
      } else {
         String errorMessage = httpResponse.headers()
                 .firstValue(REDUCT_ERROR_HEADER)
                 .orElse("Failed to create bucket");
         throw new ReductException(errorMessage, httpResponse.statusCode());
      }
   }

   private HttpResponse<String> sendRequest(HttpRequest getRequest) {
      try {
         return httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
      } catch (IOException e) {
         throw new ReductSDKException("An error occurred while processing the request", e);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new ReductSDKException("Thread has been interrupted while processing the request", e);
      }
   }

   private ServerInfo parseServerInfo(String serverInfo) {
      try {
         return objectMapper.readValue(serverInfo, ServerInfo.class);
      } catch (JacksonException e) {
         throw new ReductSDKException("The server returned a malformed response.");
      }
   }
}
