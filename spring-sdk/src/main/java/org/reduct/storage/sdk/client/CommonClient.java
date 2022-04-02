package org.reduct.storage.sdk.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Slf4j
public abstract class CommonClient {

    private final RestOperations rest;

    public CommonClient() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(1200000);
        clientHttpRequestFactory.setReadTimeout(1200000);
        rest = new RestTemplate(clientHttpRequestFactory);
    }

    @Retryable(value = {RestClientException.class, ResourceAccessException.class, HttpClientErrorException.class, HttpServerErrorException.class})
    protected <RESPONSE, BODY> RESPONSE makeRequest(
            final BODY body,
            final UriComponentsBuilder builder,
            final HttpMethod method,
            Class<RESPONSE> responseType
    ) {
        String uriString = builder.build().toUriString();
        try{
            ResponseEntity<RESPONSE> response = rest.exchange(
                    uriString,
                    method,
                    new HttpEntity<>(body, getHttpHeaders()),
                    responseType
            );

            if(Objects.nonNull(response.getBody())) {
                return response.getBody();
            }
        }
        catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error("Failed to send request by url " + uriString + "\nResponse: " + e.getResponseBodyAsString(), e);
            if(!HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                throw e;
            }
        }
        catch (Exception e) {
            log.error("Failed to send request by url " + uriString, e);
            throw e;
        }
        String msg = "Failed to send request by url " + uriString + "\n Response contains null body.";
        log.error(msg);
        throw new RuntimeException(msg);
    }

    protected HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }
}
