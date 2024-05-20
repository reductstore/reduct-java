package org.reduct.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.reduct.client.ReductClient;
import org.reduct.common.BucketURL;
import org.reduct.model.mapper.BucketMapper;
import org.reduct.utils.JsonUtils;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.reduct.utils.Strings.isNotBlank;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@Getter
@Setter
public class Bucket {
    public Bucket(String name, ReductClient reductClient) {
        this.name = name;
        this.reductClient = reductClient;
    }
    private ReductClient reductClient;
    /**
     * Name of the bucket
     */
    @JsonProperty("name")
    private String name;
    /**
     * Number of entries in the bucket
     */
    @JsonProperty("entry_count")
    private Integer entryCount;
    /**
     * Size of stored data in the bucket in bytes
     */
    @JsonProperty("size")
    private Integer size;
    /**
     * Unix timestamp of oldest record in microseconds
     */
    @JsonProperty("oldest_record")
    private BigInteger oldestRecord;
    /**
     * Unix timestamp of latest record in microseconds
     */
    @JsonProperty("latest_record")
    private BigInteger latestRecord;
    /**
     *
     */
    @JsonProperty("is_provisioned")
    private Boolean isProvisioned;
    @JsonProperty("settings")
    private BucketSettings bucketSettings;
    @SuppressWarnings("unchecked")
    @JsonProperty("info")
    private void unpackInfo(Map<String,Object> info) {
        this.name = info.get("name").toString();
        this.entryCount = Integer.valueOf(info.get("entry_count").toString());
        this.size = Integer.valueOf(info.get("size").toString());
        this.oldestRecord = new BigInteger(info.get("oldest_record").toString());
        this.latestRecord = new BigInteger(info.get("latest_record").toString());
        this.isProvisioned = Boolean.getBoolean(info.get("is_provisioned").toString());
    }

    @JsonProperty("entries")
    private List<EntryInfo> entryInfos;

    public void read() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        String createBucketPath = BucketURL.GET_BUCKET.getUrl().formatted(name);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(reductClient.getServerProperties().getBaseUrl(), createBucketPath)))
                .GET();
        if(isNotBlank(reductClient.getToken())) {
            builder.headers("Authorization", "Bearer %s".formatted(reductClient.getToken()));
        }
        HttpResponse<String> httpResponse = reductClient.send(builder, HttpResponse.BodyHandlers.ofString());
        Bucket read = JsonUtils.parseObject(httpResponse.body(), Bucket.class);
        BucketMapper.INSTANCE.copy(this, read);
    }

    public void write() {

    }
}
