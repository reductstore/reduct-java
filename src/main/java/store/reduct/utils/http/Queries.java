package store.reduct.utils.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class Queries {
	private final Map<String, Object> map;

	public Queries(String name, Object value) {
		map = new LinkedHashMap<>();
		add(name, value);
	}

	public Queries add(String name, Object value) {
		map.put(name, value);
		return this;
	}

	@Override
	public String toString() {
		return "?" + map.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
				.reduce((l, r) -> l + "&" + r).orElse(null);
	}

	public String buildQuery() {
		return toString();
	}
}
