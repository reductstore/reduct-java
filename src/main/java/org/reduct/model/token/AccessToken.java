package org.reduct.model.token;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an access token.
 *
 * @param value     The hashed value of the token
 * @param createdAt The date and time when the token was created in ISO 8601 format
 */
public record AccessToken(String value, @JsonProperty("created_at") String createdAt) {
}
