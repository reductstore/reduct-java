package org.reduct.client;

import org.reduct.client.config.ServerProperties;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.utils.http.Method;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.reduct.utils.Strings.isNotBlank;

/**
 * Base class for all clients.
 */
public abstract class ReductClient {
   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";
   abstract ServerProperties getServerProperties();
   abstract String getToken();
   abstract HttpClient getHttpClient();

   <T> HttpResponse<String> sendRequest(String url, Method method) throws ReductException, ReductSDKException {
      URI uri = URI.create("%s/%s".formatted(getServerProperties().getBaseUrl(), url));
      HttpRequest.Builder builder = HttpRequest.newBuilder()
              .uri(uri);
      switch (method) {
         case GET -> builder.GET();
         case HEAD -> builder.method("HEAD", HttpRequest.BodyPublishers.noBody());
      }

      if(isNotBlank(getToken())) {
         builder.header("Authorization", "Bearer %s".formatted(getToken()));
      }
      try {
         HttpResponse<String> httpResponse = getHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString());
         if (httpResponse.statusCode() == 200) {
            return httpResponse;

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
}
