package org.reduct.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.reduct.common.exception.ReductSDKException;

import java.util.List;

@UtilityClass
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static <T> T parseObject(String jsonBody, Class<T> tClass) {
        try {
            return objectMapper.readValue(jsonBody, tClass);
        } catch (JacksonException e) {
            throw new ReductSDKException("The server returned a malformed response.");
        }
    }
    public static <T> List<T> parseObjectAsList(String jsonBody, Class<T> tClass) {
        try {
            return objectMapper.readValue(jsonBody, objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
        } catch (JacksonException e) {
            throw new ReductSDKException("The server returned a malformed response.");
        }
    }
}
