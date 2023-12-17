package org.reduct.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class Bucket {
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

}
