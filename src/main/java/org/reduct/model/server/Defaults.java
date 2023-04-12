package org.reduct.model.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.reduct.model.bucket.BucketSettings;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Defaults {

   @JsonProperty("bucket")
   private BucketSettings bucketSettings;
}
