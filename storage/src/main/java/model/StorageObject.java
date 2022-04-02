package model;

import org.reduct.common.types.ValueObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class StorageObject implements ValueObject {

    private Object obj;

    public StorageObject(Object storageObject) {
        this.obj = storageObject;
    }

    public byte[] getByte() throws IOException {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);)
        {
            oos.writeObject(obj);
            oos.flush();
            return byteArrayOutputStream.toByteArray();
        }
    };
}
