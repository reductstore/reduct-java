package org.reduct.model.bucket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class BucketSettings {

   @JsonProperty("max_block_size")
   private Integer maxBlockSize;

   @JsonProperty("max_block_records")
   private Integer maxBlockRecords;

   @JsonProperty("quota_type")
   @JsonFormat(shape = JsonFormat.Shape.STRING)
   private QuotaType quotaType;

   @JsonProperty("quota_size")
   private Integer quotaSize;

}
