package stepdefinitions.requestMoney;

import io.ari.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.LinkedList;

import static com.google.common.collect.Lists.newLinkedList;
import static org.junit.Assert.assertEquals;

@Component
@Scope("cucumber-glue")
public class MoneyRequestReceptionsRegistry {

	public void add(String id) {
		ids.add(id);
	}

	public String getLast() {
		return ids.getLast();
	}

	public void deleteAll() {
		ids.forEach(this::delete);
	}

	private void delete(String commandId) {
		Response response = restClient.delete("commands/" + commandId);
		assertEquals("Command DELETE must return a 204.", 204, response.getStatus());
	}

	private LinkedList<String> ids = newLinkedList();

	@Autowired
	private RestClient restClient;

}
