package stepdefinitions.response;

import io.kuwagata.CucumberContext;
import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenTheResponseMustBe {

	@Then("^the response must be \"([^\"]*)\"$")
	public void the_response_must_be(String statusCode) {
		Response response = (Response) cucumberContext.getValue("response");
		assertEquals("The response must be the expected", Status.valueOf(statusCode).getStatusCode(), response.getStatus());
	}

	@Autowired
	private CucumberContext cucumberContext;
}
