package store.reduct.model.token;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a collection of access tokens.
 *
 * @param tokens collection of AccessToken
 */
public record AccessTokens(@JsonProperty("tokens") List<AccessToken> tokens) {}
