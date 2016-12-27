package io.ari;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Component
public class RestJsonReader {

	public Map<String, Object> read(Response response) {

		if (!response.hasEntity()) {
			return null;
		}

		try {
			return response.readEntity(new GenericType<Map<String, Object>>() {
			});
		} catch (ProcessingException e) {
			return ImmutableMap.of("notAJsonError", response.getStatus());
		}
	}
}
