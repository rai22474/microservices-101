package stepdefinitions.commands;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "cucumber-glue")
public class CommandsRegistry {

	public void deleteAll() {
		commandIds.forEach(this::delete);
	}

	public Collection<String> getAll() {
		return commandIds;
	}

	public void putIndexByCustomer(String customerId, Map<String, Object> command) {
		commandsByCustomer.put(customerId, command);
		add((String) command.get("id"));
	}

	public void add(String commandId) {
		commandIds.add(commandId);
	}

	public Map<String, Object> findByCustomer(String customerId) {
		return commandsByCustomer.get(customerId);
	}

	public void putIndexedById(String testId, String entityId) {
		indexedById.put(testId,entityId);
	}

	public String getById(String testId) {
		return indexedById.get(testId);
	}
	
	private void delete(String commandId) {
		deleteSubCommandsFrom(commandId);
		Response response = dataLoaderClient.delete("commands/" + commandId);
		assertEquals("The command " + commandId + " must be correctly deleted", 204, response.getStatus());
	}

	private void deleteSubCommandsFrom(String commandId) {
		Response subCommandsResponse = dataLoaderClient.get("commands", "$filter=sourceCommand eq '" + commandId + "'");
		if (subCommandsResponse.getStatus() == 204) {
			return;
		}

		Map<String, Object> subCommandsEntity = restJsonReader.read(subCommandsResponse);
		((List<Map<String, Object>>) subCommandsEntity.get("items"))
				.stream()
				.map(command -> (String) command.get("id"))
				.forEach(this::delete);
	}

	public String getLast() {
		return commandIds.getLast();
	}

	@Autowired
	private RestClient dataLoaderClient;

	@Autowired
	private RestJsonReader restJsonReader;

	private Map<String, Map<String, Object>> commandsByCustomer = new HashMap<>();

	private Map<String,String> indexedById = new HashMap<>();
	
	private LinkedList<String> commandIds = new LinkedList<>();



}
