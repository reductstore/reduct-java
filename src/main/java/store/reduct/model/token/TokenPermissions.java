package store.reduct.model.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class TokenPermissions {

	@JsonProperty("full_access")
	private Boolean fullAccess;

	@JsonProperty("read")
	private List<String> read;

	@JsonProperty("write")
	private List<String> write;
}
