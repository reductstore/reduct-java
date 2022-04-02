package org.reduct.storage.sdk.client.reduct;

import lombok.Getter;
import lombok.Setter;
import org.reduct.storage.sdk.client.CommonClient;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

public class ClientRecording extends CommonClient {

    @Getter
    @Setter
    private String url;

    String setEntry(
            String bucketName,
            String entryName,
            Long timestamp,
            Object body
    ) {
        UriComponentsBuilder builder = fromHttpUrl(this.getUrl() + "/b/" + bucketName + "/" + entryName + "?ts=" + timestamp.toString());
        return makeRequest(body, builder, HttpMethod.POST, String.class);
    };

    @GetMapping(value = "/b/{bucket_name}/{entry_name}")
    String getEntry(
            @PathVariable(name = "bucket_name") String bucketName,
            @PathVariable(name = "entry_name") String entryName
    ) {
        return "";
    };

    @GetMapping(value = "/b/{bucket_name}/{entry_name}/list")
    List<String> getEntries(
            @PathVariable(name = "bucket_name") String bucketName,
            @PathVariable(name = "entry_name") String entryName,
            @RequestParam(name = "start") Integer startTimestamp,
            @RequestParam(name = "stop") Integer stopTimestamp
    ) {
        return Collections.EMPTY_LIST;
    };
}
