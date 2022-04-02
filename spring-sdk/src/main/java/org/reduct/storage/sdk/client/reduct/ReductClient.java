package org.reduct.storage.sdk.client.reduct;

import lombok.Getter;
import lombok.Setter;
import org.reduct.storage.sdk.client.CommonClient;

/**
 * Default client for reduct storage
 */
public abstract class ReductClient extends CommonClient {

    @Getter
    @Setter
    private String url;

    public ReductClient(String url) {
        this.url = url;
    }

//    @GetMapping(value = "/info")
//    String getInfo();
//
//    @PostMapping(value = "/b/{bucket_name}/{entry_name}")
//    String setEntry(
//            @PathVariable(name = "bucket_name") String bucketName,
//            @PathVariable(name = "entry_name") String entryName,
//            Integer timestamp
//    );
//
//    @GetMapping(value = "/b/{bucket_name}/{entry_name}")
//    String getEntry(
//            @PathVariable(name = "bucket_name") String bucketName,
//            @PathVariable(name = "entry_name") String entryName
//    );
//
//    @GetMapping(value = "/b/{bucket_name}/{entry_name}/list")
//    List<String> getEntries(
//            @PathVariable(name = "bucket_name") String bucketName,
//            @PathVariable(name = "entry_name") String entryName,
//            @RequestParam(name = "start") Integer startTimestamp,
//            @RequestParam(name = "stop") Integer stopTimestamp
//    );
}
