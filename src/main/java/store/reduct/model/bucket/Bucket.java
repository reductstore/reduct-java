package store.reduct.model.bucket;

import static store.reduct.utils.http.HttpHeaders.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import lombok.*;
import org.apache.commons.lang3.ArrayUtils;
import store.reduct.client.ReductClient;
import store.reduct.common.BucketURL;
import store.reduct.common.RecordURL;
import store.reduct.common.exception.ReductException;
import store.reduct.model.mapper.BucketMapper;
import store.reduct.model.record.QueryId;
import store.reduct.model.record.Record;
import store.reduct.utils.JsonUtils;
import store.reduct.utils.Strings;
import store.reduct.utils.http.Queries;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Getter
@Setter
public class Bucket {

	private static final String TS = "ts";
	public static final String BUCKET_NAME_CANNOT_BE_NULL_OR_EMPTY = "Bucket name cannot be null or empty.";
	public static final String X_REDUCT_TIME_IS_NOT_SUCH_LONG_FORMAT = "Received from server x-reduct-time is not such Long format, or empty.";
	public static final String CONTENT_TYPE_IS_NOT_SET_IN_THE_RECORD = "The Content-Type is not set in the record.";
	public static final String CONTENT_LENGTH_IS_NOT_SET_IN_THE_RECORD = "The Content-Length is not set in the record.";

	public Bucket(String name, ReductClient reductClient) {
		this.name = name;
		this.reductClient = reductClient;
	}
	@JsonIgnore
	private ReductClient reductClient;
	/**
	 * Name of the bucket
	 */
	@JsonProperty("name")
	private String name;
	/**
	 * Number of entries in the bucket
	 */
	@JsonProperty("entry_count")
	private Integer entryCount;
	/**
	 * Size of stored data in the bucket in bytes
	 */
	@JsonProperty("size")
	private Integer size;
	/**
	 * Unix timestamp of oldest record in microseconds
	 */
	@JsonProperty("oldest_record")
	private BigInteger oldestRecord;
	/**
	 * Unix timestamp of latest record in microseconds
	 */
	@JsonProperty("latest_record")
	private BigInteger latestRecord;
	/**
	 *
	 */
	@JsonProperty("is_provisioned")
	private Boolean isProvisioned;
	@JsonProperty("settings")
	private BucketSettings bucketSettings;
	@SuppressWarnings("unchecked")
	@JsonProperty("info")
	private void unpackInfo(Map<String, Object> info) {
		this.name = info.get("name").toString();
		this.entryCount = Integer.valueOf(info.get("entry_count").toString());
		this.size = Integer.valueOf(info.get("size").toString());
		this.oldestRecord = new BigInteger(info.get("oldest_record").toString());
		this.latestRecord = new BigInteger(info.get("latest_record").toString());
		this.isProvisioned = Boolean.getBoolean(info.get("is_provisioned").toString());
	}

	@JsonProperty("entries")
	private List<EntryInfo> entryInfos;

	/**
	 * Get information about a bucket The method returns the current settings,
	 * stats, and entry list of the bucket in JSON format. If authentication is
	 * enabled, the method needs a valid API token.
	 * 
	 * @return Returns this Bucket object with updated fields
	 */
	public Bucket read() throws ReductException, IllegalArgumentException {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException(BUCKET_NAME_CANNOT_BE_NULL_OR_EMPTY);
		}
		String createBucketPath = BucketURL.GET_BUCKET.getUrl().formatted(name);
		HttpRequest.Builder builder = HttpRequest.newBuilder()
				.uri(URI.create("%s/%s".formatted(reductClient.getServerProperties().url(), createBucketPath))).GET();
		HttpResponse<String> httpResponse = reductClient.sendAndGetOnlySuccess(builder,
				HttpResponse.BodyHandlers.ofString());
		BucketMapper.INSTANCE.copy(this, JsonUtils.parseObject(httpResponse.body(), Bucket.class));
		return this;
	}

	/**
	 * To update settings of a bucket, the request should have a JSON document with
	 * all the settings.
	 * 
	 * @param bucketSettings
	 * @throws ReductException
	 *             If, unable to create the bucket. The instance of the exception
	 *             holds the error message returned in the x-reduct-error header and
	 *             the status code to indicate the failure. Some status codes: 401
	 *             -> Access token is invalid or was not provided. 403 -> Access
	 *             token does not have required permissions. 409 -> Bucket with this
	 *             name already exists. 422 -> Invalid request. 500 -> Internal
	 *             server error.
	 * @throws ReductException
	 *             If, any client side error occur.
	 * @throws IllegalArgumentException
	 *             If, the bucket name is null or empty.
	 */
	@JsonIgnore
	public void setSettings(BucketSettings bucketSettings) throws ReductException, IllegalArgumentException {
		String createBucketPath = BucketURL.CREATE_BUCKET.getUrl().formatted(name);
		URI uri = URI.create("%s/%s".formatted(reductClient.getServerProperties().url(), createBucketPath));
		HttpRequest.Builder httpRequest = HttpRequest.newBuilder().uri(uri)
				.PUT(HttpRequest.BodyPublishers.ofString(JsonUtils.serialize(bucketSettings)));
		reductClient.sendAndGetOnlySuccess(httpRequest, HttpResponse.BodyHandlers.discarding()); // TODO ask about
																									// default settings.
																									// The
		// answer from DB always is empty for
		// success, but settings sets as
		// default. This bucket will always have
		// settings as null until invoke read.
	}

	/**
	 * The method updates the Bucket and returns updated BucketSettings. If
	 * authentication is enabled, the method needs a valid API token.
	 * 
	 * @return BucketSettings
	 */
	@JsonIgnore
	public BucketSettings getSettings() {
		return this.read().getBucketSettings();
	}
	/**
	 * Write a record to an entry.
	 * 
	 * @param record
	 * @throws ReductException
	 * @throws ReductException
	 * @throws IllegalArgumentException
	 */
	public void writeRecord(@NonNull Record record) throws ReductException, IllegalArgumentException {
		// TODO validation block
		if (isNotValidRecord(record)) {
			throw new ReductException("Validation error");
		}
		Long timestamp = Objects.nonNull(record.getTimestamp()) && record.getTimestamp() > 0
				? record.getTimestamp()
				: Instant.now().getNano() / 1000;

		URI uri = URI.create(reductClient.getServerProperties().url()
				+ String.format(RecordURL.WRITE_ENTRY.getUrl(), name, record.getEntryName())
				+ new Queries(TS, timestamp));
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri).header(getContentTypeHeader(), record.getType())
				.POST(HttpRequest.BodyPublishers.ofByteArray(record.getBody()));

		reductClient.sendAndGetOnlySuccess(builder, HttpResponse.BodyHandlers.ofString()).body();
	}

	/**
	 * Write batch of records
	 * 
	 * @param entryName
	 * @param records
	 * @throws ReductException
	 * @throws IllegalArgumentException
	 */
	public void writeRecords(String entryName, Iterator<Record> records)
			throws ReductException, IllegalArgumentException {
		URI uri = URI.create(reductClient.getServerProperties().url()
				+ String.format(RecordURL.WRITE_ENTRY_BATCH.getUrl(), name, entryName));
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri);

		byte[] body = null;
		while (records.hasNext()) {
			Record record = records.next();
			// TODO validation block
			if (isNotValidRecord(record)) {
				throw new ReductException("Validation error");
			}
			byte[] byteBodyArray = record.getBody();
			body = ArrayUtils.addAll(body, byteBodyArray);
			builder.header(getXReductTimeWithNumberHeader(record.getTimestamp()),
					byteBodyArray.length + "," + record.getType());
		}
		if (Objects.nonNull(body)) {
			builder.POST(HttpRequest.BodyPublishers.ofByteArray(body));
			reductClient.sendAndGetOnlySuccess(builder, HttpResponse.BodyHandlers.ofString()).body();
		}
	}

	/**
	 * Get a record from an entry.
	 * 
	 * @param entryName
	 * @param timestamp
	 * @return
	 * @throws ReductException
	 * @throws IllegalArgumentException
	 */
	public Record readRecord(String entryName, Long timestamp) throws ReductException, IllegalArgumentException {
		if (Strings.isBlank(name) || Strings.isBlank(entryName)) {
			throw new ReductException("Validation error");
		}
		String timeStampQuery = Objects.isNull(timestamp) ? "" : new Queries(TS, timestamp).toString();
		URI uri = URI.create(reductClient.getServerProperties().url()
				+ String.format(RecordURL.GET_ENTRY.getUrl(), name, entryName) + timeStampQuery);
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri).GET();
		HttpResponse<byte[]> httpResponse = reductClient.sendAndGetOnlySuccess(builder,
				HttpResponse.BodyHandlers.ofByteArray());
		return Record.builder().body(httpResponse.body()).entryName(entryName)
				.timestamp(httpResponse.headers().firstValue(getXReductTimeHeader()).map(Long::parseLong)
						.orElseThrow(() -> new ReductException(X_REDUCT_TIME_IS_NOT_SUCH_LONG_FORMAT)))
				.type(httpResponse.headers().firstValue(getContentTypeHeader())
						.orElseThrow(() -> new ReductException(CONTENT_TYPE_IS_NOT_SET_IN_THE_RECORD)))
				.length(httpResponse.headers().firstValue(getContentLengthHeader()).map(Integer::parseInt)
						.orElseThrow(() -> new ReductException(CONTENT_LENGTH_IS_NOT_SET_IN_THE_RECORD)))
				.build();
	}

	/**
	 * Get only meta information about record.
	 * 
	 * @param entryName
	 * @param timestamp
	 * @return
	 * @throws ReductException
	 * @throws IllegalArgumentException
	 */
	public Record getMetaInfo(String entryName, Long timestamp) throws ReductException, IllegalArgumentException {
		if (Strings.isBlank(name) || Strings.isBlank(entryName)) {
			throw new ReductException("Validation error");
		}
		String timeStampQuery = Objects.isNull(timestamp) ? "" : new Queries(TS, timestamp).toString();
		URI uri = URI.create(reductClient.getServerProperties().url()
				+ String.format(RecordURL.GET_ENTRY.getUrl(), name, entryName) + timeStampQuery);
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri).method("HEAD",
				HttpRequest.BodyPublishers.noBody());
		HttpResponse<byte[]> httpResponse = reductClient.sendAndGetOnlySuccess(builder,
				HttpResponse.BodyHandlers.ofByteArray());
		return Record.builder().entryName(entryName)
				.timestamp(httpResponse.headers().firstValue(getXReductTimeHeader()).map(Long::parseLong)
						.orElseThrow(() -> new ReductException(X_REDUCT_TIME_IS_NOT_SUCH_LONG_FORMAT)))
				.type(httpResponse.headers().firstValue(getContentTypeHeader())
						.orElseThrow(() -> new ReductException(CONTENT_TYPE_IS_NOT_SET_IN_THE_RECORD)))
				.length(httpResponse.headers().firstValue(getContentLengthHeader()).map(Integer::parseInt)
						.orElseThrow(() -> new ReductException(CONTENT_LENGTH_IS_NOT_SET_IN_THE_RECORD)))
				.build();
	}

	/**
	 * Query records for a time interval
	 * 
	 * @param entryName
	 * @param start
	 * @param stop
	 * @param ttl
	 * @return
	 */
	public Iterator<Record> query(String entryName, Long start, Long stop, Long ttl)
			throws ReductException, IllegalArgumentException {
		if (Strings.isBlank(name) || Strings.isBlank(entryName) || Objects.isNull(start) || Objects.isNull(stop)
				|| Objects.isNull(ttl)) {
			throw new ReductException("Validation error");
		}
		URI uri = URI.create(
				reductClient.getServerProperties().url() + String.format(RecordURL.QUERY.getUrl(), name, entryName)
						+ new Queries("start", start).add("stop", stop).add("ttl", ttl));
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri).GET();
		HttpResponse<String> response = reductClient.sendAndGetOnlySuccess(builder,
				HttpResponse.BodyHandlers.ofString());
		QueryId queryId = JsonUtils.parseObject(response.body(), QueryId.class);

		return new RecordIterator(name, entryName, queryId.getId(), reductClient.getServerProperties().url());
	}

	public Iterator<Record> getMetaInfos(String entryName, Long start, Long stop, Long ttl)
			throws ReductException, IllegalArgumentException {
		if (Strings.isBlank(name) || Strings.isBlank(entryName) || Objects.isNull(start) || Objects.isNull(stop)
				|| Objects.isNull(ttl)) {
			throw new ReductException("Validation error");
		}
		URI uri = URI.create(
				reductClient.getServerProperties().url() + String.format(RecordURL.QUERY.getUrl(), name, entryName)
						+ new Queries("start", start).add("stop", stop).add("ttl", ttl));
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri).GET();
		HttpResponse<String> response = reductClient.sendAndGetOnlySuccess(builder,
				HttpResponse.BodyHandlers.ofString());
		QueryId queryId = JsonUtils.parseObject(response.body(), QueryId.class);

		return new MetaInfoIterator(name, entryName, queryId.getId(), reductClient.getServerProperties().url());
	}

	private boolean isNotValidRecord(Record val) {
		return Strings.isBlank(val.getEntryName()) || val.getTimestamp() <= 0 || Objects.isNull(val.getBody());
	}

	private class RecordIterator implements Iterator<Record> {
		@Getter(AccessLevel.PACKAGE)
		private final HttpRequest.Builder builder;
		private final String recordEntryName;
		@Getter(AccessLevel.PACKAGE)
		private final PriorityBlockingQueue<HeaderInstance> headerInstances = new PriorityBlockingQueue<>(8,
				Comparator.comparingLong(instance -> instance.ts));
		private byte[] body;
		@Setter(AccessLevel.PACKAGE)
		@Getter(AccessLevel.PACKAGE)
		private boolean last = false;
		boolean hasNextRecord() {
			return !headerInstances.isEmpty();
		}
		private RecordIterator(String bucketName, String recordEntryName, Long queryId, String baseUrl) {
			this(recordEntryName, queryId, HttpRequest.newBuilder()
					.uri(URI.create(baseUrl + String.format(RecordURL.GET_ENTRIES.getUrl(), bucketName, recordEntryName)
							+ new Queries("q", queryId)))
					.GET());
		}

		private RecordIterator(String recordEntryName, Long queryId, HttpRequest.Builder builder) {
			if (Objects.isNull(queryId)) {
				throw new ReductException("Validation error: queryId is null");
			}
			this.recordEntryName = recordEntryName;
			this.builder = builder;
		}

		@Override
		public boolean hasNext() {
			if (hasNextRecord()) {
				return true;
			}
			if (!isLast()) {
				HttpResponse<byte[]> httpResponse = reductClient.send(builder, HttpResponse.BodyHandlers.ofByteArray());
				if (httpResponse.statusCode() != 204) {
					body = httpResponse.body();
					int offset = 0;
					for (Map.Entry<String, List<String>> ent : httpResponse.headers().map().entrySet()) {
						if (ent.getKey().contains(getXReductTimeWithUnderscoreHeader())) {
							String ts = ent.getKey().substring(getXReductTimeWithUnderscoreHeader().length());
							String[] split = ent.getValue().get(0).split(",");
							if (split.length < 2) {
								throw new ReductException(
										String.format("Headers has a wrong format for timestamp: %s", ts));
							}
							try {
								int length = Integer.parseInt(split[0]);
								String type = split[1];
								headerInstances.put(HeaderInstance.builder()
										.ts(Optional.of(ts).map(Long::parseLong).orElseThrow(
												() -> new ReductException(X_REDUCT_TIME_IS_NOT_SUCH_LONG_FORMAT)))
										.type(type).length(length).offset(offset).build());
								offset += length;
							} catch (NumberFormatException ex) {
								throw new ReductException(CONTENT_LENGTH_IS_NOT_SET_IN_THE_RECORD);
							}
						}
					}
				} else {
					setLast(true);
				}
			}
			return hasNextRecord();
		}

		@Override
		public Record next() {
			if (last) {
				throw new NoSuchElementException();
			} else if (Objects.isNull(body) || !hasNextRecord()) {
				throw new ReductException("Invoke hasNext() method first");
			}
			HeaderInstance instance = headerInstances.poll();
			ByteBuffer byteBuffer = ByteBuffer.wrap(body);
			byte[] nextBody = new byte[instance.length];
			byteBuffer.position(instance.getOffset());
			byteBuffer.get(nextBody, 0, instance.getLength());

			return Record.builder().body(nextBody).entryName(recordEntryName).timestamp(instance.getTs())
					.type(instance.getType()).length(instance.getLength()).build();
		}
	}

	private class MetaInfoIterator extends RecordIterator {

		private MetaInfoIterator(String bucketName, String recordEntryName, Long queryId, String baseUrl) {
			super(bucketName, recordEntryName, queryId, baseUrl);

		}
		private MetaInfoIterator(String recordEntryName, Long queryId, HttpRequest.Builder builder) {
			super(recordEntryName, queryId, builder.method("HEAD", HttpRequest.BodyPublishers.noBody()));
		}

		@Override
		public boolean hasNext() {
			if (hasNextRecord()) {
				return true;
			}
			if (!isLast()) {
				HttpResponse<byte[]> httpResponse = reductClient.send(getBuilder(),
						HttpResponse.BodyHandlers.ofByteArray());
				if (httpResponse.statusCode() != 204) {
					for (Map.Entry<String, List<String>> ent : httpResponse.headers().map().entrySet()) {
						if (ent.getKey().contains(getXReductTimeWithUnderscoreHeader())) {
							String ts = ent.getKey().substring(getXReductTimeWithUnderscoreHeader().length());
							String[] split = ent.getValue().get(0).split(",");
							if (split.length < 2) {
								throw new ReductException(
										String.format("Headers has a wrong format for timestamp: %s", ts));
							}
							try {
								int length = Integer.parseInt(split[0]);
								String type = split[1];
								getHeaderInstances().put(HeaderInstance.builder()
										.ts(Optional.of(ts).map(Long::parseLong).orElseThrow(
												() -> new ReductException(X_REDUCT_TIME_IS_NOT_SUCH_LONG_FORMAT)))
										.type(type).length(length).build());
							} catch (NumberFormatException ex) {
								throw new ReductException(CONTENT_LENGTH_IS_NOT_SET_IN_THE_RECORD);
							}
						}
					}
				} else {
					setLast(true);
				}
			}
			return hasNextRecord();
		}
	}

	@Data
	@Builder
	private static class HeaderInstance {
		Long ts;
		String type;
		int length;
		int offset;
	}
}
