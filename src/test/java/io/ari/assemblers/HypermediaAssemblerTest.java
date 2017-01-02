package io.ari.assemblers;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HypermediaAssemblerTest {

	@Test
	public void shouldReturnAMapWithTheLinkInformation() {
		HypermediaAssembler hypermediaAssembler = new HypermediaAssembler();
		Map<String, Object> link = hypermediaAssembler.createLink(TEST_URI, TEST_GET_METHOD,TEST_API);

		assertNotNull("The link must be not null", link);
	}

	@Test
	public void shouldHaveAnMethodWithTheUri() {
		HypermediaAssembler hypermediaAssembler = new HypermediaAssembler();
		Map<String, Object> link = hypermediaAssembler.createLink(TEST_URI, TEST_GET_METHOD,TEST_API);

		assertEquals("The method link is not the expected", TEST_GET_METHOD, link.get("method"));
	}

	@Test
	public void shouldHaveAnHrefWithTheUri() {
		HypermediaAssembler hypermediaAssembler = new HypermediaAssembler();
		Map<String, Object> link = hypermediaAssembler.createLink(TEST_URI, TEST_GET_METHOD,TEST_API);

		assertEquals("The uri link is not the expected", TEST_URI, link.get("href"));
	}

	@Test
	public void shouldHaveAnAPI() {
		HypermediaAssembler hypermediaAssembler = new HypermediaAssembler();
		Map<String, Object> link = hypermediaAssembler.createLink(TEST_URI, TEST_GET_METHOD,TEST_API);

		assertEquals("The api link is not the expected", TEST_API, link.get("api"));
	}

	@Test
	public void shouldCanCreateASelfLink() {
		HypermediaAssembler hypermediaAssembler = new HypermediaAssembler();
		Map<String, Object> link = hypermediaAssembler.createSelfLink(TEST_URI,TEST_API);

		assertNotNull("The link must be not null", link);
		assertEquals("The api link is not the expected", TEST_API, link.get("api"));
		assertEquals("The uri link is not the expected", TEST_URI, link.get("href"));
		assertEquals("The method link is not the expected", TEST_GET_METHOD, link.get("method"));
	}

	@Test
	public void shouldCanCreateAHypermediaSection() {
		HypermediaAssembler hypermediaAssembler = new HypermediaAssembler();
		Map<String, Object> links = hypermediaAssembler.createHypermedia(TEST_URI,TEST_API);

		assertNotNull("The link must be not null", links);
		assertNotNull("Must have a self link", links.get("self"));
	}

	private static final String TEST_GET_METHOD = "GET";

	private static final String TEST_URI = "/anyUri";

	private static final String TEST_API = "ari-read";

}
