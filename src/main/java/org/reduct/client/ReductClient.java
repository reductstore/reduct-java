package org.reduct.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reduct.client.config.ServerProperties;
import org.reduct.common.exception.ReductSDKException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Base class for all clients.
 */
public class ReductClient {

   protected final ServerProperties serverProperties;
   protected final HttpClient httpClient;
   protected final ObjectMapper objectMapper;
   protected final String token;

   ReductClient(ServerProperties serverProperties, HttpClient httpClient, ObjectMapper objectMapper, String accessToken) {
      if (serverProperties == null) {
         throw new IllegalArgumentException("ServerProperties cannot be null.");
      }
      this.serverProperties = serverProperties;
      this.httpClient = httpClient;
      this.objectMapper = objectMapper;
      this.token = accessToken;
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
