package stepdefinitions.cards;

import cucumber.api.java.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:cucumber.xml")
public class DeleteCardsHook {

	@After("@deleteCard")
	public void deleteCards() {
		//cardsRegistry.deleteAll();
	}

	@Autowired
	private CardsRegistry cardsRegistry;

}
