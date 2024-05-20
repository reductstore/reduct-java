package org.reduct.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.SerializationUtils;
import org.reduct.client.config.ServerProperties;
import org.reduct.common.EntryURL;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.bucket.Bucket;
import org.reduct.model.entry.Entry;
import org.reduct.utils.Strings;
import org.reduct.utils.http.HttpHeaders;

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

import static org.reduct.utils.Strings.isNotBlank;
import static org.reduct.utils.http.Query.TIME_STAMP;

@Getter
public class ReductEntryClient extends ReductClient implements EntryClient {

    protected final ServerProperties serverProperties;
    protected final HttpClient httpClient;
    protected final String token;

    ReductEntryClient(ServerProperties serverProperties, HttpClient httpClient, String accessToken) {
        this.httpClient = httpClient;
        this.serverProperties = serverProperties;
        this.token = accessToken;
    }

    public ReductEntryClient(ServerProperties serverProperties, String accessToken) {
        this(serverProperties, HttpClient.newHttpClient(), accessToken);
    }

    @Override
    public String writeRecord(@NonNull Bucket bucket, @NonNull Entry<?> entry) throws ReductException, ReductSDKException, IllegalArgumentException {
        //TODO validation block
        if(Strings.isBlank(bucket.getName())
           || Strings.isBlank(entry.getName())
           || Objects.isNull(entry.getTimestamp())
           || entry.getTimestamp() <= 0
           || Objects.isNull(entry.getBody()))
        {
            throw new ReductSDKException("Validation error");
        }
        String timeStampQuery = getTimestampQuery(entry.getTimestamp());
        URI uri = URI.create(getServerProperties().getBaseUrl() + String.format(EntryURL.WRITE_ENTRY.getUrl(), bucket.getName(), entry.getName()) + timeStampQuery);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE.getValue(), entry.getBodyClass())
                .POST(HttpRequest.BodyPublishers.ofByteArray(entry.getByteBodyArray()));

        if(isNotBlank(getToken())) {
            builder.headers("Authorization", "Bearer %s".formatted(getToken()));
        }
        return send(builder, HttpResponse.BodyHandlers.ofString()).body();
    }

    @Override
    public <T extends Serializable> Entry<T> getRecord(Bucket bucket, Entry<?> entry) throws ReductException, ReductSDKException, IllegalArgumentException {
        if(Strings.isBlank(bucket.getName()) || Strings.isBlank(entry.getName()))
        {
            throw new ReductSDKException("Validation error");
        }
        Long timestamp = entry.getTimestamp();
        String timeStampQuery = Objects.isNull(timestamp) ? "" : getTimestampQuery(timestamp);
        URI uri = URI.create(getServerProperties().getBaseUrl() + String.format(EntryURL.GET_ENTRY.getUrl(), bucket.getName(), entry.getName()) + timeStampQuery);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .GET();
        HttpResponse<byte[]> httpResponse = send(builder, HttpResponse.BodyHandlers.ofByteArray());
        Object b = SerializationUtils.deserialize(httpResponse.body());
        return (Entry<T>) Entry.builder()
                .body((T) SerializationUtils.deserialize(httpResponse.body()))
                .name(entry.getName())
                .timestamp(httpResponse.headers().firstValue("x-reduct-time").map(Long::getLong).orElse(null))
                .build();
    }

    private String getTimestampQuery(Long timestamp) {
        return String.format("?" + TIME_STAMP.getValue() +"=%d", timestamp);
    }
}
