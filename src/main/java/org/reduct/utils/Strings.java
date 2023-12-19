package org.reduct.utils;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class Strings {
    public static boolean isBlank(String value) {
        return Objects.isNull(value) || value.isBlank();
    }
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }
}
