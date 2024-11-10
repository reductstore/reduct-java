package store.reduct.model.bucket;

import static store.reduct.utils.http.HttpHeaders.getContentTypeHeader;

import java.net.URI;
import java.net.http.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import store.reduct.client.ReductClient;
import store.reduct.client.config.ServerProperties;
import store.reduct.common.RecordURL;
import store.reduct.model.record.Record;
import store.reduct.utils.http.Queries;

@ExtendWith(MockitoExtension.class)
class BucketTest {

	@Mock
	ReductClient client;
	@Test
	@DisplayName("Should invoke sendAndGetOnlySuccess method of inner ReductClient object with according url, headers and body")
	void test1() {
		// Init
		byte[] expectedBody = "test expectedBody".getBytes();
		String expectedBucketName = "Test_bucket";
		String expectedRecordName = "Test_record_name";
		String expectedType = "Test type";
		String expectedUrl = "http://testurl";
		long timestamp = Long.MAX_VALUE - 100000;

		ServerProperties serverProperties = ServerProperties.builder().url(expectedUrl).build();

		HttpRequest.Builder builder = HttpRequest.newBuilder()
				.uri(URI.create(expectedUrl
						+ String.format(RecordURL.WRITE_ENTRY.getUrl(), expectedBucketName, expectedRecordName)
						+ new Queries("ts", timestamp)))
				.header(getContentTypeHeader(), expectedType)
				.POST(HttpRequest.BodyPublishers.ofByteArray(expectedBody));

		Bucket bucket = new Bucket(expectedBucketName, client);

		Record record = Record.builder().body(expectedBody).type(expectedType).timestamp(timestamp).build();

		Mockito.when(client.getServerProperties()).thenReturn(serverProperties);

		// Act
		bucket.writeRecord(expectedRecordName, record);

		// Assert
		Mockito.verify(client).sendAndGetOnlySuccess(Mockito.argThat(bld -> bld.copy().build().equals(builder.build())),
				Mockito.any());
	}
}
