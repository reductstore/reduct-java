package org.reduct.client;

import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.server.ServerInfo;

public interface ServerClient {

   /**
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
}
