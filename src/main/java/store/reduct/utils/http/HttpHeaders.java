package store.reduct.utils.http;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpHeaders {
	private final static String X_REDUCT_TIME = "x-reduct-time";
	private final static String X_REDUCT_TIME_ = X_REDUCT_TIME + "-";
	private final static String X_REDUCT_TIME_S = X_REDUCT_TIME_ + "%s";
	private final static String CONTENT_TYPE = "Content-Type";
	private final static String CONTENT_LENGTH = "Content-Length";
	private final static String START = "start";
	private final static String STOP = "stop";
	private static final String TTL = "ttl";

	public static String getXReductTimeHeader() {
		return X_REDUCT_TIME;
	}
	public static String getXReductTimeWithUnderscoreHeader() {
		return X_REDUCT_TIME_;
	}
	public static String getXReductTimeWithNumberHeader(Long timestamp) {
		return X_REDUCT_TIME_S + timestamp.toString();
	}
	public static String getContentTypeHeader() {
		return CONTENT_TYPE;
	}
	public static String getContentLengthHeader() {
		return CONTENT_LENGTH;
	}
	public static String getStartHeader() {
		return START;
	}
	public static String getStopHeader() {
		return STOP;
	}
	public static String getTtlHeader() {
		return TTL;
	}
}
