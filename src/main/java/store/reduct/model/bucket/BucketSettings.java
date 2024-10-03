package store.reduct.model.bucket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Configuration for a bucket
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class BucketSettings {

	/**
	 * Max block size in bytes
	 */
	@JsonProperty("max_block_size")
	private Integer maxBlockSize;

	/**
	 * Max number of records in a block
	 */
	@JsonProperty("max_block_records")
	private Integer maxBlockRecords;

	/**
	 * Quota type
	 */
	@JsonProperty("quota_type")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private QuotaType quotaType;

	/**
	 * Quota size in bytes
	 */
	@JsonProperty("quota_size")
	private Integer quotaSize;

	/**
	 * The client raises no exception if the bucket already exists and returns it
	 */
	@JsonIgnore
	private Boolean exists;
}
