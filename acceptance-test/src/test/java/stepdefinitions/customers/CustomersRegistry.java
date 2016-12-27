package stepdefinitions.customers;

import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;

@Component
@Scope("cucumber-glue")
public class CustomersRegistry {

	public void registerCustomer(String id, String code) {
		customersByCode.put(code, id);
	}

	public void deleteRegisterCustomers() {
		customersByCode.keySet()
				.stream()
				.map(customersByCode::get)
				.forEach(this::deleteClient);
	}

	private void deleteClient(String customerIdentifier) {
		Response response = restClient.delete("customers/" + customerIdentifier);
		assertEquals("The customer:" + customerIdentifier + " -> must be correctly deleted", 204, response.getStatus());
	}

	@Autowired
	private RestClient restClient;

	private Map<String, String> customersByCode = newHashMap();
}
