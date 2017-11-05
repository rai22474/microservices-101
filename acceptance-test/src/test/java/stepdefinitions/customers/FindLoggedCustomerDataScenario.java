package stepdefinitions.customers;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration("classpath:cucumber.xml")
public class FindLoggedCustomerDataScenario {


	@Given("^the customer \"(.*?)\" is correct logged$")
	public void the_customer_is_correct_logged(String customerIdCard) {
		cucumberContext.publishValue("loggedUserIdCard", customerIdCard);
	}

	@When("^i request the current customer data$")
	public void i_request_the_current_customer_data() {
		Map<String, Object> headers = new HashMap<>();

		Map<String, Object> customerIdentifiers = (Map<String, Object>) cucumberContext.getValue("customerIdentifiers");

		String customerIdCard = (String) cucumberContext.getValue("loggedUserIdCard");
		String customerIdentifier = (String) customerIdentifiers.get(customerIdCard);

		headers.put("x-customer-id", customerIdentifier != null ? customerIdentifier : NO_EXISTING_CUSTOMER);
		response = requestSender.get("me", headers);

		cucumberContext.publishValue("response", response);
		cucumberContext.publishValue("responseEntity", restJsonReader.read(response));
		cucumberContext.publishValue("idCard", customerIdCard);
	}

	@Given("^the customer \"(.*?)\" is correct logged but don't exists in the active customers$")
	public void the_customer_is_correct_logged_but_don_t_exists_in_the_active_customers(String customerCode) {
		cucumberContext.publishValue("customerCode", customerCode);
	}

	@Then("^have the customer common data$")
	public void have_the_customer_common_data() {
		Map<String, Object> customerData = (Map<String, Object>) cucumberContext.getValue("responseEntity");

		assertEquals("The customer id card is not the expected", cucumberContext.getValue("idCard"), customerData.get("idCard"));
		assertEquals("The customer name is not the expected", "Alfred", customerData.get("name"));
		assertEquals("The customer lastName is not the expected", "Hitchcock", customerData.get("lastName"));

		//checkAdress((Map<String, Object>) customerData.get("address"));
		//checkContactDetails((Collection<Map<String, Object>>) customerData.get("contactDetails"));
		checkHypermedia((Map<String, Object>) customerData.get("_links"));
	}

	private void checkHypermedia(Map<String, Object> links) {
		assertNotNull("There must have a list of links", links);

		Map<String, Object> link = (Map<String, Object>) links.get("self");

		assertNotNull("The me link must be not null", link);
		assertEquals("The link has the href", "api/me", link.get("href"));

		Map<String, Object> bucks = (Map<String, Object>) links.get("bucks");

		assertNotNull("The me link must be not null", bucks);
		assertEquals("The link has the href", "api/bucks", bucks.get("href"));

	}

	private void checkAdress(Map<String, Object> address) {
		assertEquals("The customer addressType is not the expected", "calle", address.get("addressType"));
		assertEquals("The customer streetAddress is not the expected", "Maldonado", address.get("streetAddress"));
		assertEquals("The customer streetNumber is not the expected", 1, address.get("streetNumber"));
		assertEquals("The customer houseNumber is not the expected", 3, address.get("houseNumber"));
		assertEquals("The customer houseLetter is not the expected", "A", address.get("houseLetter"));
		assertEquals("The customer postcode is not the expected", "28006", address.get("postcode"));
		assertEquals("The customer town is not the expected", "Madrid", address.get("town"));
	}

	private void checkContactDetails(Collection<Map<String, Object>> contactDetails) {
		assertNotNull("The contact details must be not null", contactDetails);

		assertNotNull("The contact details must have a list of items", contactDetails);
		assertEquals("There number of items is not correct", 2, contactDetails.size());

		Iterator<Map<String, Object>> iterator = contactDetails.iterator();
		Map<String, Object> firstContactDetail = iterator.next();
		Map<String, Object> secondContactDetail = iterator.next();

		assertEquals("The first contact is a email", "personal_email", firstContactDetail.get("contactType"));
		assertEquals("The second contact is a phone", "personal_phone", secondContactDetail.get("contactType"));
	}

	private static final String NO_EXISTING_CUSTOMER = "535433dd3004da28a45301ab";

	@Autowired
	private RestClient requestSender;

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private RestJsonReader restJsonReader;

	private Response response;
}
