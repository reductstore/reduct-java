package org.reduct.storage.sdk.client.reduct;

import org.reduct.storage.sdk.AuthorizationService;
import org.reduct.storage.sdk.config.ReductSettings;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

public class ReductClientInfoImpl extends ReductClient implements ReductClientInfo {

    public ReductClientInfoImpl(AuthorizationService authorizationService, ReductSettings settings) {
        super(authorizationService, settings);
    }

    @Override
    public String getInfo() {
        UriComponentsBuilder builder = fromHttpUrl(this.getSettings().getUrl() + "/info");
        return makeRequest(null, builder, HttpMethod.GET, String.class);
    }
}
