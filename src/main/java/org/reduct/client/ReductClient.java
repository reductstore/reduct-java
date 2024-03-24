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
   private static final String ERROR_400 = "Posted content bigger or smaller than content-length";
   private static final String ERROR_401 = "Access token is invalid or empty";
   private static final String ERROR_403 = "Access token does not have write permissions";
   private static final String ERROR_404 = "Bucket is not found";
   private static final String ERROR_409 = "A record with the same timestamp already exists";
   private static final String ERROR_422 = "Bad timestamp";
   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";
   abstract ServerProperties getServerProperties();
   abstract String getToken();
   abstract HttpClient getHttpClient();

   <T> HttpResponse<String> sendRequest(String url, Method method) throws ReductException, ReductSDKException {
      URI uri = URI.create("%s/%s".formatted(getServerProperties().getBaseUrl(), url));
      HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri);
      switch (method) {
         case GET -> builder.GET();
         case HEAD -> builder.method("HEAD", HttpRequest.BodyPublishers.noBody());
      }

      if(isNotBlank(getToken())) {
         builder.headers("Authorization", "Bearer %s".formatted(getToken()));
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

   public <T> T send(HttpRequest.Builder builder, HttpResponse.BodyHandler<T> bodyHandler) {
      try {
         HttpResponse<T> httpResponse = getHttpClient().send(builder.build(), bodyHandler);
         switch (httpResponse.statusCode()) {
            case 200 -> {
               return httpResponse.body();
            }
            case 400 -> throw new ReductException(ERROR_400, httpResponse.statusCode());
            case 401 -> throw new ReductException(ERROR_401, httpResponse.statusCode());
            case 403 -> throw new ReductException(ERROR_403, httpResponse.statusCode());
            case 404 -> throw new ReductException(ERROR_404, httpResponse.statusCode());
            case 409 -> throw new ReductException(ERROR_409, httpResponse.statusCode());
            case 422 -> throw new ReductException(ERROR_422, httpResponse.statusCode());
            default -> throw new ReductException("Unsuccessful request", httpResponse.statusCode());
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
