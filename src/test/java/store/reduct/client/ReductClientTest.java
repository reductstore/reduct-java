package store.reduct.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Map;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.reduct.client.config.ServerProperties;
import store.reduct.common.exception.ReductException;
import store.reduct.model.bucket.Bucket;
import store.reduct.model.bucket.BucketSettings;
import store.reduct.utils.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ReductClientTest {

	@Nested
	class BucketSettingsTest {
		@Mock
		private HttpClient httpClient;
		@Mock
		HttpResponse httpResponse;

		@ParameterizedTest(name = "Should return bucket with an expected name, client and settings if exists = {0}")
		@CsvSource(value = {"200, null", "200, false"}, nullValues = "null")
		void test1(Integer statusCode, Boolean exists) throws IOException, InterruptedException {
			// Init
			String expectedBucketName = "testBucket";
			BucketSettings expectedBucketSettings = BucketSettings.builder().maxBlockSize(Integer.MAX_VALUE)
					.exists(exists).build();
			ReductClient expectedClient = new ReductClient(ServerProperties.builder().url("http://testUrl").build(),
					httpClient);
			when(httpClient.send(any(), any())).thenReturn(httpResponse);
			when(httpResponse.statusCode()).thenReturn(statusCode);
			// Act
			Bucket testBucket = expectedClient.createBucket(expectedBucketName, expectedBucketSettings);
			// Assert
			assertThat(testBucket.getBucketSettings()).isEqualTo(expectedBucketSettings);
			assertThat(testBucket.getName()).isEqualTo(expectedBucketName);
			assertThat(testBucket.getReductClient()).isEqualTo(expectedClient);
		}
		@ParameterizedTest(name = "Should throw an exception if status = {0}")
		@EnumSource(value = HttpStatus.class, mode = EnumSource.Mode.EXCLUDE, names = {"OK", "CONFLICT"})
		void test2(HttpStatus status) throws IOException, InterruptedException {
			// Init
			String expectedBucketName = "testBucket";
			BucketSettings expectedBucketSettings = BucketSettings.builder().maxBlockSize(Integer.MAX_VALUE)
					.exists(true).build();
			ReductClient expectedClient = new ReductClient(ServerProperties.builder().url("http://testUrl").build(),
					httpClient);
			when(httpClient.send(any(), any())).thenReturn(httpResponse);
			when(httpResponse.statusCode()).thenReturn(status.getCode());
			when(httpResponse.headers()).thenReturn(HttpHeaders.of(Map.of(), (l, r) -> true));
			// Act
			AbstractThrowableAssert<?, ? extends Throwable> anAssert = assertThatThrownBy(
					() -> expectedClient.createBucket(expectedBucketName, expectedBucketSettings));
			// Assert
			anAssert.isInstanceOf(ReductException.class);
		}
		@ParameterizedTest(name = "Should return bucket with an expected name, client and settings if status = {0}")
		@EnumSource(value = HttpStatus.class, mode = EnumSource.Mode.INCLUDE, names = {"OK", "CONFLICT"})
		void test3(HttpStatus status) throws IOException, InterruptedException {
			// Init
			String expectedBucketName = "testBucket";
			BucketSettings expectedBucketSettings = BucketSettings.builder().maxBlockSize(Integer.MAX_VALUE)
					.exists(true).build();
			ReductClient expectedClient = new ReductClient(ServerProperties.builder().url("http://testUrl").build(),
					httpClient);
			when(httpClient.send(any(), any())).thenReturn(httpResponse);
			when(httpResponse.statusCode()).thenReturn(status.getCode());
			// Act
			Bucket testBucket = expectedClient.createBucket(expectedBucketName, expectedBucketSettings);
			// Assert
			assertThat(testBucket.getBucketSettings()).isEqualTo(expectedBucketSettings);
			assertThat(testBucket.getName()).isEqualTo(expectedBucketName);
			assertThat(testBucket.getReductClient()).isEqualTo(expectedClient);
		}
	}
}
