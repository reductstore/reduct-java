package org.reduct.model.server;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.reduct.model.bucket.QuotaType;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class DefaultBucketSettings {

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
