package org.reduct.model.entry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Entry<BODY_CLASS extends Serializable> {
    private String name;
    private Long timestamp;
    private final BODY_CLASS body;

    public Entry(BODY_CLASS body, Long timestamp) {
        this.timestamp = timestamp;
        this.body = body;
    }

    public String getBodyClass() {
        return body.getClass().getSimpleName();
    }
    public byte[] getByteBodyArray() {
        return SerializationUtils.serialize(body);
    }
}
