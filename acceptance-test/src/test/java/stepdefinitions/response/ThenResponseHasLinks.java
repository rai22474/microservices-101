package stepdefinitions.response;


import cucumber.api.java.en.Then;
import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.Assert.*;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenResponseHasLinks {

	@Then("^the response has links to$")
	public void the_response_has_links_to(List<Map<String, String>> expectedLinks) {
		Map<String, Map<String, String>> links = getResponseLinks();
		assertFalse("Returned response links cannot be empty.", links.isEmpty());

		expectedLinks
				.stream()
				.forEach(verifyExpectedLink(links));
	}

	@Then("^the response only has links to$")
	public void the_response_only_has_links_to(List<Map<String, String>> expectedLinks) {
		Map<String, Map<String, String>> links = getResponseLinks();
		assertFalse("Returned response links cannot be empty.", links.isEmpty());
		assertEquals("Returned response links must have the number of expected links.", expectedLinks.size(), links.size());

		expectedLinks
				.stream()
				.forEach(verifyExpectedLink(links));
	}

	private Map<String, Map<String, String>> getResponseLinks() {
		Map<String, Object> responseEntity = (Map<String, Object>) cucumberContext.getValue("responseEntity");

		assertTrue("Returned response must have a _links item.", responseEntity.containsKey("_links"));
		return (Map<String, Map<String, String>>) responseEntity.get("_links");
	}

	private Consumer<Map<String, String>> verifyExpectedLink(Map<String, Map<String, String>> links) {
		return expectedLink -> {
			String rel = expectedLink.get("link");
			assertTrue("Expected link to " + rel, links.containsKey(rel));

			Map<String, String> foundLink = links.get(rel);
			if (expectedLink.containsKey("api"))
				assertEquals("Expected link to " + rel + " must have the right api", expectedLink.get("api"), foundLink.get("api"));
			if (expectedLink.containsKey("href"))
				assertTrue("Expected link to " + rel + " must have the right href", foundLink.get("href").matches(expectedLink.get("href")));
		};
	}

	@Autowired
	private CucumberContext cucumberContext;

}
