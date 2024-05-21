package org.reduct.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.SerializationUtils;
import org.reduct.client.ReductClient;
import org.reduct.common.BucketURL;
import org.reduct.common.EntryURL;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.entry.Entry;
import org.reduct.model.mapper.BucketMapper;
import org.reduct.utils.JsonUtils;
import org.reduct.utils.Strings;
import org.reduct.utils.http.HttpHeaders;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.reduct.utils.http.Query.TIME_STAMP;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Getter
@Setter
public class Bucket {
    public Bucket(String name, ReductClient reductClient) {
        this.name = name;
        this.reductClient = reductClient;
    }
    @JsonIgnore
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

    /**
     * The method returns the current settings, stats, and entry list of the bucket in JSON format. If authentication is enabled, the method needs a valid API token.
     * @return Returns this Bucket object with updated fields
     */
    public Bucket read() throws ReductException, ReductSDKException, IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        String createBucketPath = BucketURL.GET_BUCKET.getUrl().formatted(name);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(reductClient.getServerProperties().getBaseUrl(), createBucketPath)))
                .GET();
        HttpResponse<String> httpResponse = reductClient.send(builder, HttpResponse.BodyHandlers.ofString());
        Bucket read = JsonUtils.parseObject(httpResponse.body(), Bucket.class);
        BucketMapper.INSTANCE.copy(this, read);
        return this;
    }

    /**
     * Create a new bucket with the name and settings.
     * NOTE: If, authentication is enabled on the server, an access token with full access must be provided
     * to create a new bucket.
     * @return This bucket
     * @throws ReductException          If, unable to create the bucket. The instance of the exception holds
     *                                  the error message returned in the x-reduct-error header and the
     *                                  status code to indicate the failure.
     *                                  Some status codes:
     *                                  401 -> Access token is invalid or was not provided.
     *                                  403 -> Access token does not have required permissions.
     *                                  409 -> Bucket with this name already exists.
     *                                  422 -> Invalid request.
     *                                  500 -> Internal server error.
     * @throws ReductSDKException       If, any client side error occur.
     * @throws IllegalArgumentException If, the bucket name is null or empty.
     */
    public Bucket write() throws ReductException, ReductSDKException, IllegalArgumentException {
        String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(name);
        URI uri = URI.create("%s/%s".formatted(reductClient.getServerProperties().getBaseUrl(), createBucketPath));
        HttpRequest.Builder httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(JsonUtils.serialize(bucketSettings)));
        reductClient.send(httpRequest, HttpResponse.BodyHandlers.discarding()); //TODO ask about default settings. The answer from DB always is empty for success, but settings sets as default. This bucket will always have settings as null until invoke read.
        return this;
    }

    /**
     * Write a record to an entry.
     * @param entry
     * @throws ReductException
     * @throws ReductSDKException
     * @throws IllegalArgumentException
     */
    public void writeRecord(@NonNull Entry<?> entry) throws ReductException, ReductSDKException, IllegalArgumentException {
        //TODO validation block
        if(Strings.isBlank(entry.getName())
                || Objects.isNull(entry.getTimestamp())
                || entry.getTimestamp() <= 0
                || Objects.isNull(entry.getBody()))
        {
            throw new ReductSDKException("Validation error");
        }
        String timeStampQuery = getTimestampQuery(entry.getTimestamp());
        URI uri = URI.create(reductClient.getServerProperties().getBaseUrl() + String.format(EntryURL.WRITE_ENTRY.getUrl(), name, entry.getName()) + timeStampQuery);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE.getValue(), entry.getBodyClass())
                .POST(HttpRequest.BodyPublishers.ofByteArray(entry.getByteBodyArray()));

        reductClient.send(builder, HttpResponse.BodyHandlers.ofString()).body();
    }

    /**
     * Get a record from an entry.
     * @param entry
     * @return
     * @param <T>
     * @throws ReductException
     * @throws ReductSDKException
     * @throws IllegalArgumentException
     */
    public <T extends Serializable> Entry<T> getRecord(Entry<?> entry) throws ReductException, ReductSDKException, IllegalArgumentException {
        if(Strings.isBlank(name) || Strings.isBlank(entry.getName()))
        {
            throw new ReductSDKException("Validation error");
        }
        Long timestamp = entry.getTimestamp();
        String timeStampQuery = Objects.isNull(timestamp) ? "" : getTimestampQuery(timestamp);
        URI uri = URI.create(reductClient.getServerProperties().getBaseUrl() + String.format(EntryURL.GET_ENTRY.getUrl(), name, entry.getName()) + timeStampQuery);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .GET();
        HttpResponse<byte[]> httpResponse = reductClient.send(builder, HttpResponse.BodyHandlers.ofByteArray());
        return (Entry<T>) Entry.builder()
                .body((T) SerializationUtils.deserialize(httpResponse.body()))
                .name(entry.getName())
                .timestamp(httpResponse.headers().firstValue("x-reduct-time").map(Long::getLong).orElse(null))
                .build();
    }

    /**
     * Get only meta information about record.
     * @param entry
     * @return
     * @param <T>
     * @throws ReductException
     * @throws ReductSDKException
     * @throws IllegalArgumentException
     */
    public <T extends Serializable> Entry<T> getMetaInfo(Entry<?> entry) throws ReductException, ReductSDKException, IllegalArgumentException {
        if(Strings.isBlank(name) || Strings.isBlank(entry.getName()))
        {
            throw new ReductSDKException("Validation error");
        }
        Long timestamp = entry.getTimestamp();
        String timeStampQuery = Objects.isNull(timestamp) ? "" : getTimestampQuery(timestamp);
        URI uri = URI.create(reductClient.getServerProperties().getBaseUrl() + String.format(EntryURL.GET_ENTRY.getUrl(), name, entry.getName()) + timeStampQuery);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .method("HEAD", HttpRequest.BodyPublishers.noBody());
        HttpResponse<byte[]> httpResponse = reductClient.send(builder, HttpResponse.BodyHandlers.ofByteArray());
        return (Entry<T>) Entry.builder()
                .name(entry.getName())
                .timestamp(httpResponse.headers().firstValue("x-reduct-time").map(Long::valueOf).orElse(null))
                .build();
    }

    private String getTimestampQuery(Long timestamp) {
        return String.format("?" + TIME_STAMP.getValue() +"=%d", timestamp);
    }
}
