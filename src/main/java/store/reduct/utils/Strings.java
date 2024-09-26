package store.reduct.utils;

import java.util.Objects;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Strings {
	public static boolean isBlank(String value) {
		return Objects.isNull(value) || value.isBlank();
	}
	public static boolean isNotBlank(String value) {
		return !isBlank(value);
	}
}
