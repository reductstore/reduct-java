package org.reduct.utils.http;

import lombok.Getter;

import java.util.function.Function;

public enum Query {
    TIME_STAMP("ts");

    @Getter
    private String value;
    Query(String value) {
        this.value = value;
    }

}
