package org.reduct.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reduct.client.config.ServerProperties;
import org.reduct.common.BucketURL;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.bucket.Bucket;
import org.reduct.model.bucket.BucketSettings;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ReductClient2 {
    private static final String REDUCT_ERROR_HEADER = "x-reduct-error";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final ServerProperties serverProperties;
    private final HttpClient httpClient;
    private final String token;

    public ReductClient2(ServerProperties serverProperties, HttpClient httpClient, String token) {
        this.serverProperties = serverProperties;
        this.httpClient = httpClient;
        this.token = token;
    }

    public Bucket createBucket(String bucketName, BucketSettings bucketSettings) throws ReductException, ReductSDKException, IllegalArgumentException {
        if (bucketName == null || bucketName.isBlank()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        String requestBody = serializeSettingsOrEmptyJson(bucketSettings);
        HttpRequest httpRequest = createHttpRequest(bucketName, requestBody);
        HttpResponse<Void> httpResponse = executeHttpRequest(httpRequest);
        if (httpResponse.statusCode() == 200) {
            return Bucket.builder().name(bucketName).bucketSettings(bucketSettings).build();
        } else {
            String errorMessage = httpResponse.headers()
                    .firstValue(REDUCT_ERROR_HEADER)
                    .orElse("Failed to create bucket");
            throw new ReductException(errorMessage, httpResponse.statusCode());
        }
    }

    private String serializeSettingsOrEmptyJson(BucketSettings bucketSettings) {
        if (bucketSettings == null) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(bucketSettings);
        } catch (JsonProcessingException e) {
            throw new ReductSDKException("Failed to serialize bucket settings", e);
        }
    }
    private HttpRequest createHttpRequest(String bucketName, String requestBody) {
        return HttpRequest.newBuilder()
                .uri(constructCreateBucketUri(bucketName))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Authorization", "Bearer %s".formatted(token))
                .build();
    }
    private URI constructCreateBucketUri(String bucketName) {
        String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(bucketName);
        return URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createBucketPath));
    }

    private HttpResponse<Void> executeHttpRequest(HttpRequest httpRequest) {
        try {
            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
        } catch (IOException e) {
            throw new ReductSDKException("An error occurred while processing the request", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ReductSDKException("Thread has been interrupted while processing the request", e);
        }
    }
}
