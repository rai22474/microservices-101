package stepdefinitions.response;


import cucumber.api.java.en.Then;
import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenResponseHasData {

	@Then("^the response has the following data$")
	public void the_response_has_the_following_data(List<Map<String, Object>> expectedData) {
		Map<String, Object> expectedContent = expectedData.stream().findFirst().get();

		Map<String, Object> returnedContent = (Map<String, Object>) cucumberContext.getValue("responseEntity");
		expectedContent.forEach(verifyContentProperty(returnedContent));
	}

	private BiConsumer<? super String, ? super Object> verifyContentProperty(Map<String, Object> returnedContent) {
		return (key, value) -> {
			assertTrue("Returned content must contain a " + key + ".", returnedContent.containsKey(key));
			assertEquals("Returned content " + key + " must be the expected.", value, returnedContent.get(key));
		};
	}

	@Autowired
	private CucumberContext cucumberContext;

}
