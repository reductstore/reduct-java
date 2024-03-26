package org.reduct.client;

import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.bucket.Bucket;
import org.reduct.model.entry.Entry;

import java.io.Serializable;

public interface EntryClient {
    /**
     * Write a record to an entry
     */
    String writeRecord(Bucket bucket, Entry<?> body) throws ReductException, ReductSDKException, IllegalArgumentException;
    <T extends Serializable> Entry<T> getRecord(Bucket bucket, Entry<?> entry) throws ReductException, ReductSDKException, IllegalArgumentException;
}
