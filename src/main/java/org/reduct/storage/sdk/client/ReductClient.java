package org.reduct.storage.sdk.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "reduct",
        url = "${application.url}"
)
public interface ReductClient {

    @GetMapping(value = "/info")
    public String getInfo();
}
