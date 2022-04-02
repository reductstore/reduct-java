package org.reduct.storage.sdk.client.reduct;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.reduct.storage.sdk.client.CommonClient;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Component
public class BucketClient extends CommonClient {

    @Getter
    @Setter
    private String url;

    public BucketClient() {
        url = "http://127.0.0.1:8383";
    }

    public String getBucketInfo(String buckedName) {
        UriComponentsBuilder builder = fromHttpUrl(this.getUrl() + "/b/"+ buckedName);
        return makeRequest(null, builder, HttpMethod.GET, String.class);
    }

    public String isExistBucket(String buckedName) {
        UriComponentsBuilder builder = fromHttpUrl(this.getUrl() + "/b/"+ buckedName);
        return makeRequest(null, builder, HttpMethod.HEAD, String.class);
    }

    @SneakyThrows
    public String createBucket(String buckedName, Integer maxBlockSize, QuotaType quotaType, Integer quotaSize) {
        ObjectMapper objectMapper = new ObjectMapper();
        BucketBody bucketBody = new BucketBody(maxBlockSize, quotaType, quotaSize);
        String body = objectMapper.writeValueAsString(bucketBody);
        UriComponentsBuilder builder = fromHttpUrl(this.getUrl() + "/b/"+ buckedName);

        return makeRequest(body, builder, HttpMethod.POST, String.class);
    }

    @SneakyThrows
    public String updateBucket(String buckedName, Integer maxBlockSize, QuotaType quotaType, Integer quotaSize) {
        ObjectMapper objectMapper = new ObjectMapper();
        BucketBody bucketBody = new BucketBody(maxBlockSize, quotaType, quotaSize);
        String body = objectMapper.writeValueAsString(bucketBody);
        UriComponentsBuilder builder = fromHttpUrl(this.getUrl() + "/b/"+ buckedName);

        return makeRequest(body, builder, HttpMethod.PUT, String.class);
    }

    public Object removeBucket(String buckedName) {
        UriComponentsBuilder builder = fromHttpUrl(this.getUrl() + "/b/"+ buckedName);
        return makeRequest(null, builder, HttpMethod.PUT, Object.class);
    }

}
