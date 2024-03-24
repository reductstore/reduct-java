package org.reduct.client;

import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.bucket.Bucket;
import org.reduct.model.entry.Entry;

public interface EntryClient {
    /**
     * Write a record to an entry
     */
    String writeRecord(Bucket bucket, Entry<?> body) throws ReductException, ReductSDKException, IllegalArgumentException;
}
