package stepdefinitions.cards;

import cucumber.api.java.en.When;
import io.ari.CucumberContext;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.customers.CustomersRegistry;

import javax.ws.rs.core.Response;

@ContextConfiguration("classpath:cucumber.xml")
public class WhenBlockCard {

	@When("^the customer \"(.*?)\" blocks its tva card$")
	public void the_user_blocks_its_tva_card(String idCard) {
		String customerId = getCustomerId(idCard);
		String cardId = cardsRegistry.get(customerId, "tva");

		doBlockCard(customerId, cardId);
	}

	@When("^the customer \"(.*?)\" blocks the last created card$")
	public void the_user_blocks_the_last_created_card(String idCard) {
		doBlockCard(getCustomerId(idCard), cardsRegistry.getLast());
	}

	@When("^the customer \"(.*?)\" blocks the card \"(.*?)\"$")
	public void the_user_blocks_the_card(String idCard, String cardId) {
		doBlockCard(getCustomerId(idCard), cardId);
	}

	@When("^an unknown customer blocks the last created card$")
	public void an_unknown_user_blocks_the_last_created_card() {
		doBlockCard("db9adab0da0db", cardsRegistry.getLast());
	}

	private void doBlockCard(String customerId, String cardId) {
		Response response = cardsService.blockCard(customerId, cardId);

		cucumberContext.publishValue("response", response);
		cucumberContext.publishValue("responseEntity", restJsonReader.read(response));
	}

	private String getCustomerId(String idCard) {
		return customersRegistry.getCustomerId(idCard);
	}

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private CustomersRegistry customersRegistry;

	@Autowired
	private RestJsonReader restJsonReader;

	@Autowired
	private CardsRegistry cardsRegistry;

	@Autowired
	private CardsService cardsService;

}
