package store.reduct.model.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Record {
	private Long timestamp;
	private byte[] body;
	private String type;
	private Integer length;
}
