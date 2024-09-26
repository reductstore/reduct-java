package store.reduct.client;

import org.junit.jupiter.api.Disabled;

@Disabled
public class ReductEntryClientTest {
	// private static final String ACCESS_TOKEN = "test-access-token";
	// private static final String VALIDATION_ERROR_MESSAGE = "Validation error";
	// private ReductEntryClient client;
	// private ServerProperties serverProperties;
	// private HttpClient httpClient;
	// private HttpResponse<Void> mockHttpResponse;
	// @BeforeEach
	// public void init() {
	// serverProperties = new ServerProperties(false, "127.0.0.1", 8383);
	// httpClient = mock(HttpClient.class);
	// client = new ReductEntryClient(serverProperties, httpClient, ACCESS_TOKEN);
	// mockHttpResponse = mock(HttpResponse.class);
	// }
	//
	// @Test
	// @DisplayName("Should return empty string")
	// public void test1() throws IOException, InterruptedException {
	// // Init
	// String response = "response";
	// String bodyValue = "body value";
	// Bucket bucket = Bucket.builder().name("testBucketName").build();
	// Entry<Serializable> entry =
	// Entry.builder().name("testEntryName").timestamp(99L).body(bodyValue).build();
	// HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
	// when(mockHttpResponse.statusCode()).thenReturn(200);
	// when(mockHttpResponse.body()).thenReturn(response);
	// when(httpClient.send(argThat(builder -> {
	// return
	// builder.uri().toString().equals("http://127.0.0.1:8383/api/v1/b/testBucketName/testEntryName?ts=99")
	// && builder.method().equals("POST")
	// && builder.headers().map().get("Authorization").get(0).equals("Bearer " +
	// ACCESS_TOKEN)
	// &&
	// builder.headers().map().get("Content-Type").get(0).equals(String.class.getSimpleName());
	// }),
	// eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(mockHttpResponse);
	// // Act
	// String writeRecord = client.writeRecord(bucket, entry);
	// // Assert
	// assertEquals(response, writeRecord);
	// }
	//
	// @ParameterizedTest(name = "Should throw ReductSDKException with validation
	// error")
	// @CsvSource(value = {
	// "null, null, null, null",
	// "null, null, 0, null",
	// "someName, null, null, null",
	// "null, someName, null, null",
	// "null, null, 1, null",
	// "null, null, null, body value",
	// "null, someName, 1, body value",
	// "someName, null, 1, body value",
	// "someName, someName, 0, body value",
	// "someName, someName, null, body value",
	// "someName, someName, 1, null",
	// }, nullValues = "null")
	// public void test2(String bucketName, String entryName, Long timestamp, String
	// entryBody) {
	// // Init
	// Bucket bucket = Bucket.builder().name(bucketName).build();
	// Entry<Serializable> entry =
	// Entry.builder().name(entryName).timestamp(timestamp).body(entryBody).build();
	// // Act
	// ReductSDKException result = assertThrows(ReductSDKException.class, () ->
	// client.writeRecord(bucket, entry));
	//
	// // Assert
	// assertEquals(VALIDATION_ERROR_MESSAGE, result.getMessage());
	// }
	//
	// @ParameterizedTest(name = "Should throw ReductSDKException with validation
	// error")
	// @CsvSource(value = {
	// "null, null",
	// "null, testEntryName",
	// "testBucketName, null"
	// }, nullValues = "null")
	// public void test3(String bucketName, String entryName) {
	// // Init
	// Bucket bucket = Bucket.builder().name(bucketName).build();
	// Entry<Serializable> entry = Entry.builder().name(entryName).build();
	// // Act
	// ReductSDKException result = assertThrows(ReductSDKException.class, () ->
	// client.getRecord(bucket, entry));
	// // Assert
	// assertEquals(VALIDATION_ERROR_MESSAGE, result.getMessage());
	// }
	//
	// @Test
	// @DisplayName("Should return testBodyString")
	// public void test4() throws IOException, InterruptedException {
	// // Init
	// String response = "testBodyString";
	// String entryName = "testEntryName";
	// String bucketName = "testBucketName";
	// Bucket bucket = Bucket.builder().name(bucketName).build();
	// Entry<Serializable> entry = Entry.builder().name(entryName).build();
	// HttpResponse<byte[]> mockHttpResponse = mock(HttpResponse.class);
	// when(mockHttpResponse.statusCode()).thenReturn(200);
	// when(mockHttpResponse.body()).thenReturn(SerializationUtils.serialize(response));
	// when(mockHttpResponse.headers()).thenReturn(HttpHeaders.of(Map.of("x-reduct-time",
	// List.of()), (l, r) -> true));
	// when(httpClient.send(
	// argThat(builder ->
	// builder.uri().toString().equals("http://127.0.0.1:8383/api/v1/b/" +
	// bucketName +"/" + entryName) &&
	// builder.method().equals("GET")),
	// eq(HttpResponse.BodyHandlers.ofByteArray()))).thenReturn(mockHttpResponse);
	// // Act
	// Entry<String> record = client.getRecord(bucket, entry);
	// // Assert
	// assertEquals(response, record.getBody());
	// assertEquals(entryName, record.getName());
	// }
}
