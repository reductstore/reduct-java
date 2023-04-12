# Reduct Storage Client SDK for Java
This package provides a Java client for interacting with the [ReductStore](https://www.reduct.store) service.

## Features
* Supports the [ReductStore HTTP API v1.4](https://docs.reduct.store/http-api)
* Token-based authentication for secure access to the database

## Requirements
* Java 17 or higher

## Getting Started

To get started with the ReductStore Client SDK for Java, you'll need to have ReductStore installed and running on
your machine. You can find instructions for installing ReductStore [here](https://docs.reduct-store/#start-with-docker).

### Create a new access token
NOTE: The token you're instantiating the client with must have full access to create new tokens.

```java
import org.reduct.client.ReductTokenClient;
import org.reduct.client.TokenClient;
import org.reduct.client.config.ServerProperties;
import org.reduct.model.token.AccessToken;
import org.reduct.model.token.TokenPermissions;
import java.util.List;

public class Main {
   public static void main(String[] args) {
      ServerProperties serverProperties = new ServerProperties(false, "127.0.0.1", 8383);
      TokenClient tokenClient = new ReductTokenClient(serverProperties, "<your_api_key>");
      TokenPermissions tokenPermissions =
              TokenPermissions.of(true, List.of("test"), List.of("test"));
      AccessToken token = tokenClient.createToken("a_token_name", tokenPermissions);

      System.out.println("Token: " + token.value());
      System.out.println("Created at: " + token.createdAt());
   }
}
```

### Retrieve information about the server
```java
import org.reduct.client.ReductServerClient;
import org.reduct.client.config.ServerProperties;
import org.reduct.model.server.ServerInfo;

public class Main {
   public static void main(String[] args) {
      ServerProperties serverProperties = new ServerProperties(false, "127.0.0.1", 8383);
      ServerClient client = new ReductServerClient(serverProperties, "<your-api-key>");

      ServerInfo serverInfo = client.getServerInfo();
      
      System.out.println("Version: " + serverInfo.getVersion());
      System.out.println("Bucket Count: " + serverInfo.getBucketsCount());
   }
}
```
## References

* [ReductStore HTTP API](https://docs.reduct.store/http-api)