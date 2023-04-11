package org.reduct.client;

import org.reduct.common.exception.ReductException;
import org.reduct.model.token.AccessToken;
import org.reduct.model.token.TokenPermissions;

/**
 * Client interface for Token related operations.
 */
public interface TokenClient {

   /**
    * Attempts to create a new access token with the given permissions.
    *
    * @param tokenName   The name of the token to create.
    * @param permissions The permissions to give to the token. Such as, whether it has full access to the server,
    *                    or only read access to some buckets, or write access to some buckets.
    * @return {@link org.reduct.model.token.AccessToken} object that holds the token and the creation date.
    * @throws ReductException          If the request fails, for example the token already exists, or
    *                                  any of the buckets listed does not exist on the server.
    * @throws IllegalArgumentException If the token name is null or blank, or if the
    *                                  {@link org.reduct.model.token.TokenPermissions} object is null.
    */
   AccessToken createToken(String tokenName, TokenPermissions permissions) throws ReductException;
}
