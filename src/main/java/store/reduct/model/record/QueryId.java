package store.reduct.model.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Getter
@Setter
public class QueryId {
	@JsonProperty("id")
	Long id;
}
