# reduct-java
Reduct Storage Client SDK for Java

This package provides a Java client for interacting with the [ReductStore](https://www.reduct.store) service.

## Features
* Supports the [ReductStore HTTP API v1.4](https://docs.reduct.store/http-api)

## Requirements
* Java 17 or higher

## Examples
### Retrieve information about the server
```java
import org.reduct.client.ReductServerClient;
import org.reduct.client.config.ServerProperties;
import org.reduct.model.server.ServerInfo;

public static void main(String[] args) {
      ServerProperties serverProperties = new ServerProperties(false, "127.0.0.1", 8383);
      ReductServerClient client = new ReductServerClient(serverProperties, "<your-api-key>");

      ServerInfo serverInfo = client.getServerInfo();

      String version = serverInfo.getVersion();
      int bucketCount = serverInfo.getBucketCount();

      System.out.println("Version: " + version);
      System.out.println("Bucket Count: " + bucketCount);
}