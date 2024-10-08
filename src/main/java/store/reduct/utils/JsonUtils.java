package store.reduct.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.experimental.UtilityClass;
import store.reduct.common.exception.ReductException;
import store.reduct.model.bucket.BucketSettings;
import store.reduct.model.token.TokenPermissions;

@UtilityClass
public class JsonUtils {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	public static <T> T parseObject(String jsonBody, Class<T> tClass) {
		try {
			return objectMapper.readValue(jsonBody, tClass);
		} catch (JacksonException e) {
			throw new ReductException(e.getMessage());
		}
	}
	public static <T> List<T> parseObjectAsList(String jsonBody, Class<T> tClass) {
		try {
			return objectMapper.readValue(jsonBody,
					objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
		} catch (JacksonException e) {
			throw new ReductException("The server returned a malformed response.");
		}
	}

	public static String serializeSettingsOrEmptyJson(BucketSettings bucketSettings) {
		if (bucketSettings == null) {
			return "{}";
		}
		try {
			return objectMapper.writeValueAsString(bucketSettings);
		} catch (JsonProcessingException e) {
			throw new ReductException("Failed to serialize bucket settings", e);
		}
	}

	public static String serializeCreateTokenBody(TokenPermissions permissions) {
		try {
			return objectMapper.writeValueAsString(permissions);
		} catch (JsonProcessingException e) {
			throw new ReductException("Failed to serialize the token permissions.", e);
		}
	}

	public static String serialize(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new ReductException("Failed to serialize.", e);
		}
	}
}
