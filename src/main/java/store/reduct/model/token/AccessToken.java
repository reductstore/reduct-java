package store.reduct.model.token;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an access token.
 *
 * @param name
 *            The name of the token
 * @param createdAt
 *            The date and time when the token was created in ISO 8601 format
 */
public record AccessToken(String name, @JsonProperty("created_at") String createdAt) {
}
