package stepdefinitions.cards;

import io.ari.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

import static com.google.common.collect.ImmutableMap.of;

@Component
public class CardsService {

	public Response blockCard(String customerId, String cardId) {

		return restClient.post(
				"cards/" + cardId + "/blocks",
				null,
				of("x-customer-id", customerId));
	}

	@Autowired
	private RestClient restClient;

}
