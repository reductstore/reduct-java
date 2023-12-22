package org.reduct.client;

import lombok.AccessLevel;
import lombok.Getter;
import org.reduct.client.config.ServerProperties;
import org.reduct.common.ServerURL;
import org.reduct.common.exception.ReductException;
import org.reduct.common.exception.ReductSDKException;
import org.reduct.model.bucket.Buckets;
import org.reduct.model.server.ServerInfo;
import org.reduct.utils.JsonUtils;
import org.reduct.utils.http.HttpStatus;
import org.reduct.utils.http.Method;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Getter(value = AccessLevel.PACKAGE)
public class ReductServerClient extends ReductClient implements ServerClient {

   protected final ServerProperties serverProperties;
   protected final HttpClient httpClient;
   protected final String token;

   /**
    * Constructs a new ReductServerClient with the given properties.
    * NOTE: Client created without access token will not be able to interact with the server if,
    * authentication is enabled on the server.
    *
    * @param serverProperties The properties, such as host and port
    */
   public ReductServerClient(ServerProperties serverProperties) {
      this(serverProperties, null);
   }

   /**
    * Constructs a new ReductServerClient with the given properties and the given access token.
    *
    * @param serverProperties The properties, such as host and port
    * @param accessToken      The access token to use for authentication
    */
   public ReductServerClient(ServerProperties serverProperties, String accessToken) {
      this(serverProperties, HttpClient.newHttpClient(), accessToken);
   }

   ReductServerClient(ServerProperties serverProperties, HttpClient httpClient, String accessToken) {
      this.httpClient = httpClient;
      this.serverProperties = serverProperties;
      this.token = accessToken;
   }

   @Override
   public ServerInfo getServerInfo() throws ReductException, ReductSDKException {
      HttpResponse<String> httpResponse = sendRequest(ServerURL.SERVER_INFO.getUrl(), Method.GET);
      return JsonUtils.parseObject(httpResponse.body(), ServerInfo.class);
   }

   @Override
   public Buckets getList() throws ReductException, ReductSDKException {
      HttpResponse<String> httpResponse = sendRequest(ServerURL.LIST.getUrl(), Method.GET);
      return JsonUtils.parseObject(httpResponse.body(), Buckets.class);
   }

   @Override
   public Boolean isAlive() throws ReductException, ReductSDKException {
      HttpResponse<String> httpResponse = sendRequest(ServerURL.ALIVE.getUrl(), Method.HEAD);
      return HttpStatus.OK.getCode().equals(httpResponse.statusCode());
   }
}
