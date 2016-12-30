package stepdefinitions.response;


import cucumber.api.java.en.Then;
import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenTheResponseMustBe {

	@Then("^the response must be \"([^\"]*)\"$")
	public void the_response_must_be(String statusName) {
		Response response = (Response) cucumberContext.getValue("response");
		assertEquals("The response status must be the expected", Status.valueOf(statusName).getStatusCode(), response.getStatus());
	}

	@Then("^the response code must be \"(.*?)\"$")
	public void the_response_code_must_be(Integer statusCode) {
		Response response = (Response) cucumberContext.getValue("response");
		assertEquals("The response status code must be the expected", statusCode, (Integer) response.getStatus());
	}

	@Autowired
	private CucumberContext cucumberContext;

}
