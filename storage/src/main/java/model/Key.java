package model;

import org.reduct.common.types.ValueObject;

public class Key implements ValueObject {

    private Object key;

    public Key(Object key) {
        this.key = key;
    }

    public String getString() {
        return key.toString();
    }
}
