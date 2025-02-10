package store.reduct.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QueryOptions {
	private final Long start;
	private final Long stop;
	private final Long ttl;
}
