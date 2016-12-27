package io.ari.heartbeat;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HeartbeatResourceTest {

	@Test
	public void shouldReturnOkIfCall(){
		ResponseEntity response = new HeartbeatResource().heartbeat();
		
		assertNotNull("The response code must not null", response);
		assertEquals("The response code should be OK", 200, response.getStatusCode().value());
	}
}

