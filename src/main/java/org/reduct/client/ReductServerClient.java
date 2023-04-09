package org.reduct.client;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reduct.client.config.ServerClientProperties;
import org.reduct.common.ServerURL;
import org.reduct.common.exception.ReductException;
import org.reduct.model.server.ServerInfo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ReductServerClient implements ServerClient {

   private final ServerClientProperties serverProperties;
   private final HttpClient httpClient;
   private final ObjectMapper objectMapper;

   public ReductServerClient(ServerClientProperties serverClientProperties) {
      this(serverClientProperties, HttpClient.newHttpClient());
   }

   ReductServerClient(ServerClientProperties serverClientProperties, HttpClient client) {
      serverProperties = serverClientProperties;
      httpClient = client;
      objectMapper = new ObjectMapper();
   }

   @Override
   public ServerInfo getServerInfo() {
      URI serverInfoUri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), ServerURL.SERVER_INFO.getUrl()));
      HttpRequest getRequest = HttpRequest.newBuilder()
              .GET()
              .uri(serverInfoUri)
              .build();
      HttpResponse<String> response = sendRequest(getRequest);
      if (response.statusCode() == 200) {
         return parseServerInfo(response.body());
      } else {
         throw new ReductException("The server returned an unexpected response. Please try again later.",
                 response.statusCode());
      }
   }

   private HttpResponse<String> sendRequest(HttpRequest getRequest) {
      try {
         return httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
      } catch (IOException e) {
         throw new ReductException("An error occurred while processing the request", e);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new ReductException("Thread has been interrupted while processing the request", e);
      }
   }

   private ServerInfo parseServerInfo(String serverInfo) {
      try {
         return objectMapper.readValue(serverInfo, ServerInfo.class);
      } catch (JacksonException e) {
         throw new ReductException("The server returned a malformed response.");
      }
   }
}
