package org.reduct.storage.sdk.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class ReductSettings {

    String url;
    String secretKey;
    String clientName;
    String clientPassword;

}
