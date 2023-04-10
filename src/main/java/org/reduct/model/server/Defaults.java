package org.reduct.model.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Defaults {

   @JsonProperty("bucket")
   private DefaultBucketSettings bucketSettings;
}
