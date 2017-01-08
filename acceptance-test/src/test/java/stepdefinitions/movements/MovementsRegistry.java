package stepdefinitions.movements;

import io.ari.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;

@Component
@Scope("cucumber-glue")
public class MovementsRegistry {

	public void deleteAll() {
		movementIds.forEach(this::delete);
	}

	public void add(String movementId) {
		movementIds.add(movementId);
	}

	public void registerMovement(String index, String movementId) {
		add(movementId);
		movementsIndexed.put(index, movementId);
	}

	public String getByIndex(String index) {
		return movementsIndexed.get(index);
	}

	private void delete(String movementId) {
		Response response = restClient.delete("timeline/" + movementId);
		assertEquals("The movement " + movementId + " must be correctly deleted", 204, response.getStatus());
	}

	public void addMovement(Map<String, Object> movement) {
		movements.add(movement);
	}

	@Autowired
	private RestClient restClient;

	private List<String> movementIds = newArrayList();

	private Map<String, String> movementsIndexed = newHashMap();

	private List<Map<String, Object>> movements = newArrayList();
}
