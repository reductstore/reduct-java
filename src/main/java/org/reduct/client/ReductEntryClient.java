package org.reduct.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.reduct.client.config.ServerProperties;
import org.reduct.common.EntryURL;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.bucket.Bucket;
import org.reduct.model.entry.Entry;
import org.reduct.utils.Strings;
import org.reduct.utils.http.HttpHeaders;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

import static org.reduct.utils.Strings.isNotBlank;
import static org.reduct.utils.http.Query.TIME_STAMP;

@Getter(value = AccessLevel.PACKAGE)
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
    public String writeRecord(@NonNull Bucket bucket, @NonNull Entry<?> body) throws ReductException, ReductSDKException, IllegalArgumentException {
        //TODO validation block
        if(Strings.isBlank(bucket.getName())
           || Strings.isBlank(body.getName())
           || Objects.isNull(body.getTimestamp())
           || body.getTimestamp() <= 0
           || Objects.isNull(body.getBody()))
        {
            throw new ReductSDKException("Validation error");
        }
        String timeStampQuery = String.format("?" + TIME_STAMP.getValue() +"=%d", body.getTimestamp());
        URI uri = URI.create(getServerProperties().getBaseUrl() + String.format(EntryURL.WRITE_ENTRY.getUrl(), bucket.getName(), body.getName()) + timeStampQuery);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE.getValue(), body.getBodyClass())
                .POST(HttpRequest.BodyPublishers.ofByteArray(body.getByteBodyArray()));

        if(isNotBlank(getToken())) {
            builder.headers("Authorization", "Bearer %s".formatted(getToken()));
        }
        return send(builder, HttpResponse.BodyHandlers.ofString());
    }
}
