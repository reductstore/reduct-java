package store.reduct.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class EntryInfo {
    // entry name
    @JsonProperty("name")
    private String name;
    // disk usage in bytes
    @JsonProperty("size")
    private Integer size;
    // number of blocks with data
    @JsonProperty("block_count")
    private Integer blockCount;
    // number of records
    @JsonProperty("record_count")
    private Integer recordCount;
    // unix timestamp of oldest record in microseconds
    @JsonProperty("oldest_record")
    private Integer oldestRecord;
    // unix timestamp of latest record in microseconds
    @JsonProperty("latest_record")
    private Integer latestRecord;
}
