package org.reduct.utils.http;

import lombok.Getter;

public enum HttpHeaders {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length");
    @Getter
    private String value;
    HttpHeaders(String value) {
        this.value = value;
    }
}
