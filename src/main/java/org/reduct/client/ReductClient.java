package org.reduct.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reduct.client.config.ServerProperties;

import java.net.http.HttpClient;

/**
 * Base class for all clients.
 */
public abstract class ReductClient {

   protected final ServerProperties serverProperties;
   protected final HttpClient httpClient;
   protected final ObjectMapper objectMapper;
   protected final String token;

   protected ReductClient(ServerProperties serverProperties, HttpClient httpClient,
                          ObjectMapper objectMapper, String accessToken) {
      if (serverProperties == null) {
         throw new IllegalArgumentException("ServerProperties cannot be null.");
      }
      if (accessToken == null || accessToken.isBlank()) {
         throw new IllegalArgumentException("Access token cannot be null or empty.");
      }
      this.serverProperties = serverProperties;
      this.httpClient = httpClient;
      this.objectMapper = objectMapper;
      this.token = accessToken;
   }

}
