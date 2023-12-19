package org.reduct.client;

import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.bucket.Buckets;
import org.reduct.model.server.ServerInfo;

public interface ServerClient {

   /**
    * Get statistical information about the storage. You can use this method to get stats of the storage and check its version.
    * Attempts to retrieve the server information. Such as, version, bucket count, usage, uptime, oldest record,
    * latest record, and default settings.
    *
    * @return {@link ServerInfo} object if the request is successful.
    * @throws ReductException    if the request fails. The instance of the exception holds
    *                            the error message returned in the x-reduct-error header and the
    *                            status code to indicate the failure.
    * @throws ReductSDKException If, any client side error occurs.
    */
   ServerInfo getServerInfo() throws ReductException, ReductSDKException;

   Buckets getList() throws ReductException, ReductSDKException;
}
