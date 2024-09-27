package store.reduct.client;

import static store.reduct.utils.Strings.isNotBlank;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.reduct.client.config.ServerProperties;
import store.reduct.common.BucketURL;
import store.reduct.common.ServerURL;
import store.reduct.common.TokenURL;
import store.reduct.common.exception.ReductException;
import store.reduct.model.bucket.Bucket;
import store.reduct.model.bucket.BucketSettings;
import store.reduct.model.bucket.Buckets;
import store.reduct.model.server.ServerInfo;
import store.reduct.model.token.AccessToken;
import store.reduct.model.token.AccessTokens;
import store.reduct.model.token.TokenPermissions;
import store.reduct.utils.JsonUtils;
import store.reduct.utils.http.HttpStatus;

/**
 * Base class for all clients.
 */
@RequiredArgsConstructor
@Getter
public class ReductClient {
	private static final String REDUCT_ERROR_HEADER = "x-reduct-error";
	private final ServerProperties serverProperties;
	private final HttpClient httpClient;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public <T> HttpResponse<T> send(HttpRequest.Builder builder, HttpResponse.BodyHandler<T> bodyHandler) {
		try {
			if (isNotBlank(serverProperties.apiToken())) {
				builder.headers("Authorization", "Bearer %s".formatted(serverProperties.apiToken()));
			}
			HttpResponse<T> httpResponse = httpClient.send(builder.build(), bodyHandler);
			if (httpResponse.statusCode() == 200) {
				return httpResponse;
			}
			throw new ReductException(
					httpResponse.headers().firstValue(REDUCT_ERROR_HEADER).orElse("Unsuccessful request"),
					httpResponse.statusCode());
		} catch (IOException e) {
			throw new ReductException("An error occurred while processing the request", e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ReductException("Thread has been interrupted while processing the request", e);
		}
	}

	/**
	 * Create a new bucket with the name and the settings. NOTE: If, authentication
	 * is enabled on the server, an access token with full access must be provided
	 * to create a new bucket.
	 * 
	 * @return This bucket
	 * @throws ReductException
	 *             If, unable to create the bucket. The instance of the exception
	 *             holds the error message returned in the x-reduct-error header and
	 *             the status code to indicate the failure. Some status codes: 401
	 *             -> Access token is invalid or was not provided. 403 -> Access
	 *             token does not have required permissions. 409 -> Bucket with this
	 *             name already exists. 422 -> Invalid request. 500 -> Internal
	 *             server error.
	 * @throws IllegalArgumentException
	 *             If, the bucket name is null or empty.
	 */
	public Bucket createBucket(String name, BucketSettings bucketSettings)
			throws ReductException, IllegalArgumentException {
		String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(name);
		URI uri = URI.create("%s/%s".formatted(serverProperties.url(), createBucketPath));
		HttpRequest.Builder httpRequest = HttpRequest.newBuilder().uri(uri)
				.POST(HttpRequest.BodyPublishers.ofString(JsonUtils.serialize(bucketSettings)));
		HttpResponse<Void> httpResponse = send(httpRequest, HttpResponse.BodyHandlers.discarding());// TODO ask about
																									// default settings.
																									// The answer from
																									// DB always is
																									// empty for
																									// success, but
																									// settings sets as
																									// default. This
																									// bucket will
																									// always have
																									// settings as null
																									// until invoke
																									// read.
		if (httpResponse.statusCode() == 200) {
			Bucket bucket = new Bucket(name, this);
			bucket.setBucketSettings(bucketSettings);
			return bucket;
		} else {
			throw new ReductException(
					httpResponse.headers().firstValue(REDUCT_ERROR_HEADER).orElse("Unsuccessful request"),
					httpResponse.statusCode());
		}
	}

	/**
	 * Get statistical information about the storage. You can use this method to get
	 * stats of the storage and check its version. Attempts to retrieve the server
	 * information. Such as, version, bucket count, usage, uptime, oldest record,
	 * latest record, and default settings.
	 *
	 * @return {@link ServerInfo} object if the request is successful.
	 * @throws ReductException
	 *             if the request fails. The instance of the exception holds the
	 *             error message returned in the x-reduct-error header and the
	 *             status code to indicate the failure.
	 */
	public ServerInfo info() throws ReductException {
		URI uri = URI.create("%s/%s".formatted(getServerProperties().url(), ServerURL.SERVER_INFO.getUrl()));
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri).GET();
		HttpResponse<String> httpResponse = send(builder, HttpResponse.BodyHandlers.ofString());
		return JsonUtils.parseObject(httpResponse.body(), ServerInfo.class);
	}

	/**
	 * Get a list of the buckets with their stats
	 *
	 * @return Collection of {@link store.reduct.model.bucket.Bucket}
	 * @throws ReductException
	 *             if the request fails. The instance of the exception holds the
	 *             error message returned in the x-reduct-error header and the
	 *             status code to indicate the failure.
	 */
	public Buckets list() throws ReductException {
		URI uri = URI.create("%s/%s".formatted(getServerProperties().url(), ServerURL.LIST.getUrl()));
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri).GET();
		HttpResponse<String> httpResponse = send(builder, HttpResponse.BodyHandlers.ofString());
		return JsonUtils.parseObject(httpResponse.body(), Buckets.class);
	}

	/**
	 * Check if the storage engine is working
	 * 
	 * @return true if the server is working
	 * @throws ReductException
	 *             if the request fails. The instance of the exception holds the
	 *             error message returned in the x-reduct-error header and the
	 *             status code to indicate the failure.
	 */
	public Boolean isAlive() throws ReductException {
		URI uri = URI.create("%s/%s".formatted(getServerProperties().url(), ServerURL.ALIVE.getUrl()));
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri).method("HEAD",
				HttpRequest.BodyPublishers.noBody());
		HttpResponse<String> httpResponse = send(builder, HttpResponse.BodyHandlers.ofString());
		return HttpStatus.OK.getCode().equals(httpResponse.statusCode());
	}

	/**
	 * The method returns a list of tokens with names and creation dates. To use
	 * this method, you need an access token with full access.
	 * 
	 * @return AccessTokens
	 */
	public AccessTokens tokens() throws ReductException {
		URI uri = URI.create("%s/%s".formatted(getServerProperties().url(), TokenURL.GET_TOKENS.getUrl()));
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri).GET();
		HttpResponse<String> httpResponse = send(builder, HttpResponse.BodyHandlers.ofString());
		return JsonUtils.parseObject(httpResponse.body(), AccessTokens.class);
	}

	/**
	 * Attempts to create a new access token with the given permissions.
	 *
	 * @param tokenName
	 *            The name of the token to create.
	 * @param permissions
	 *            The permissions to give to the token. Such as, whether it has full
	 *            access to the server, or only read access to some buckets, or
	 *            write access to some buckets.
	 * @return {@link AccessToken} object that holds the token and the creation
	 *         date.
	 * @throws ReductException
	 *             If the request fails, for example the token already exists, or
	 *             any of the buckets listed does not exist on the server. The
	 *             instance of the exception holds the error message returned in the
	 *             x-reduct-error header and the status code to indicate the
	 *             failure.
	 * @throws IllegalArgumentException
	 *             If the token name is null or blank, or if the
	 *             {@link store.reduct.model.token.TokenPermissions} object is null.
	 */
	public AccessToken createToken(String tokenName, TokenPermissions permissions)
			throws ReductException, IllegalArgumentException {
		if (tokenName == null || tokenName.isBlank()) {
			throw new IllegalArgumentException("Token name must not be null or blank");
		}
		if (permissions == null) {
			throw new IllegalArgumentException("Permissions must not be null");
		}
		String createTokenPath = TokenURL.CREATE_TOKEN.getUrl().formatted(tokenName);
		String createTokenBody = JsonUtils.serialize(permissions);

		HttpRequest.Builder builder = HttpRequest.newBuilder()
				.uri(URI.create("%s/%s".formatted(serverProperties.url(), createTokenPath)))
				.POST(HttpRequest.BodyPublishers.ofString(createTokenBody));
		HttpResponse<String> response = send(builder, HttpResponse.BodyHandlers.ofString());
		return JsonUtils.parseObject(response.body(), AccessToken.class);
	}
}
