package org.reduct.storage.sdk.client.reduct;

import lombok.Getter;
import lombok.Setter;
import org.reduct.storage.sdk.AuthorizationService;
import org.reduct.storage.sdk.client.CommonClient;
import org.reduct.storage.sdk.config.ReductSettings;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.function.Supplier;

//@FeignClient(
//        name = "reduct",
//        url = "${application.url}"
//)
/**
 * Default client for reduct storage
 */
public abstract class ReductClient extends CommonClient {

    private AuthorizationService authorizationService;

    @Getter
    private ReductSettings settings;

    public ReductClient(AuthorizationService authorizationService, ReductSettings settings) {
        this.authorizationService = authorizationService;
        this.settings = settings;
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
