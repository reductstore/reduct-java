package store.reduct.common;

public enum RecordURL {
	WRITE_ENTRY("/api/v1/b/%s/%s"), WRITE_ENTRY_BATCH("/api/v1/b/%s/%s/batch"), GET_ENTRY(
			"/api/v1/b/%s/%s"), GET_ENTRIES("/api/v1/b/%s/%s/batch"), QUERY("/api/v1/b/%s/%s/q");

	private final String url;

	RecordURL(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
