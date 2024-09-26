package store.reduct.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.*;

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
