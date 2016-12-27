package stepdefinitions.customers;

import io.ari.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;


@Component
@Scope(value = "cucumber-glue")
public class DeleteCustomersHook {

	public void registerCustomers(String... codes) {
		for (String code : codes) {
			getCustomersIdsAsArrays().add(code);
		}
	}

	public Collection<String> getCustomersIdsAsArrays() {
		return customerIdsAsArrays;
	}

	public void deleteCustomers(String customerId) {
		Response response = requestSender.delete("io/ari/subjects/" + customerId);
		assertEquals("The customer:" + customerId + "-> must be correctly deleted", 204, response.getStatus());
	}

	@Autowired
	private RestClient requestSender;

	private Collection<String> customerIdsAsArrays = new ArrayList<>();
}

