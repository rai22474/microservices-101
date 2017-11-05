package stepdefinitions.cards;


import io.ari.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;

@Component
@Scope("cucumber-glue")
public class CardsRegistry {

	public String getLast() {
		return cardIds.getLast();
	}

	public String get(String customerId, String cardType) {
		return (String) cardsByCustomerAndType.get(customerId).get(cardType);
	}

	public void add(String customerId, String cardType, String id) {
		addCardToCustomer(customerId, cardType, id);
		cardIds.add(id);
	}

	public void deleteAll() {
		cardIds.forEach(this::delete);
		cardIds.clear();
	}

	private void addCardToCustomer(String customerId, String cardType, String id) {
		Map<String, Object> customerCards = cardsByCustomerAndType.getOrDefault(customerId, newHashMap());
		customerCards.put(cardType, id);
		cardsByCustomerAndType.put(customerId, customerCards);
	}

	private void delete(String cardId) {
		Response deleteResponse = dataLoaderClient.delete("agreements/" + cardId);
		assertEquals(204, deleteResponse.getStatus());
	}

	private LinkedList<String> cardIds = new LinkedList<>();

	private Map<String, Map<String, Object>> cardsByCustomerAndType = newHashMap();

	@Autowired
	private RestClient dataLoaderClient;
}
