package org.reduct.storage.sdk.client.reduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BucketBody {

    private Integer maxBlockSize;
    private QuotaType quotaType;
    private Integer quotaSize;

}
