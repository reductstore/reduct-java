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
public class ServerInfo {

   @JsonProperty("version")
   private String version;

   @JsonProperty("bucket_count")
   private int bucketCount;

   @JsonProperty("usage")
   private int usage;

   @JsonProperty("uptime")
   private int uptime;

   @JsonProperty("oldest_record")
   private String oldestRecord;

   @JsonProperty("latest_record")
   private String latestRecord;

   @JsonProperty("defaults")
   private Defaults defaults;

}
