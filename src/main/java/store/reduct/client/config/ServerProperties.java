package store.reduct.client.config;

import lombok.Builder;

@Builder
public record ServerProperties(String url, String apiToken) {
}
