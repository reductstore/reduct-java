package store.reduct.model.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

	@JsonProperty("license")
	private String license;

}
