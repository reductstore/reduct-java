package org.reduct.model.bucket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class BucketSettings {

   @JsonProperty("max_block_size")
   private int maxBlockSize;

   @JsonProperty("max_block_records")
   private int maxBlockRecords;

   @JsonProperty("quota_type")
   @JsonFormat(shape = JsonFormat.Shape.STRING)
   private QuotaType quotaType;

   @JsonProperty("quota_size")
   private int quotaSize;

}