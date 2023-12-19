package org.reduct.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class Buckets {
    /**
     * Collection of buckets
     */
    @JsonProperty("buckets")
    List<Bucket> buckets;
}
