package org.reduct.storage.sdk.client.reduct;

import lombok.Getter;
import lombok.Setter;
import org.reduct.storage.sdk.client.CommonClient;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Component
public class ClientInfo extends CommonClient {

    @Getter
    @Setter
    private String url;

    public ClientInfo() {
        url = "http://127.0.0.1:8383";
    }

    public String getInfo() {
        UriComponentsBuilder builder = fromHttpUrl(this.getUrl() + "/info");
        return makeRequest(null, builder, HttpMethod.GET, String.class);
    }

    public String getList() {
        UriComponentsBuilder builder = fromHttpUrl(this.getUrl() + "/list");
        return makeRequest(null, builder, HttpMethod.GET, String.class);
    }

}
