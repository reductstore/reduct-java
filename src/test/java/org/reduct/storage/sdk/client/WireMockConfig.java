package org.reduct.storage.sdk.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WireMockConfig {

    @Autowired
    private WireMockConfig wireMockServer;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockBooksService() {
        return new WireMockServer(8383);
    }
}
