package org.reduct.storage.sdk.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


@Component
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor(AuthorizationService authorizationService) {
        return requestTemplate -> {
            if(requestTemplate.headers().get(HttpHeaders.CONTENT_TYPE) == null) {
                requestTemplate.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            }
            requestTemplate.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            requestTemplate.header(HttpHeaders.AUTHORIZATION, authorizationService.getToken());
        };
    }
}
