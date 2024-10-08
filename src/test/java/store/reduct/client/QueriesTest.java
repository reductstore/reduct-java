package store.reduct.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import store.reduct.utils.http.Queries;

class QueriesTest {
	@Test
	void test1() {
		// Init
		Queries q = new Queries("name1", 4L).add("key2", "new").add("ttl", 60);
		// Act
		// Assert
		assertThat("?name1=4&key2=new&ttl=60").isEqualTo(q.toString());
		assertThat("?name1=4&key2=new&ttl=60").isEqualTo(q.buildQuery());
	}
	@Test
	void test2() {
		// Init
		Queries q = new Queries("key1", 4L);
		// Act
		// Assert
		assertThat("?key1=4").isEqualTo(q.buildQuery());
		assertThat("?key1=4").isEqualTo(q.toString());
	}
}
