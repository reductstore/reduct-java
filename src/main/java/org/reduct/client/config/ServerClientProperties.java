package org.reduct.client.config;

public record ServerClientProperties(boolean isSsl, String host, int port) {

   /**
    * @return The base url for the server
    */
   public String getBaseUrl() {
      return (isSsl ? "https://" : "http://") + host + ":" + port;
   }
}
