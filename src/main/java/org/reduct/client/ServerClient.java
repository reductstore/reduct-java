package org.reduct.client;

import org.reduct.model.server.ServerInfo;

public interface ServerClient {

   /**
    * Attempts to retrieve the server information. Such as, version, bucket count, usage, uptime, oldest record,
    * latest record, and default settings.
    * Returns {@link org.reduct.model.server.ServerInfo} object if the request is successful.
    * Throws {@link org.reduct.common.exception.ReductException} if the request fails.
    */
   ServerInfo getServerInfo();
}
