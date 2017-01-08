package io.ari.schemaValidations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class SchemasContainer {

	public Optional<String> getSchemaLocation(String path, String method) {
		return schemas.entrySet()
				.stream()
				.filter(entry -> path.matches(entry.getKey()))
				.map(Map.Entry::getValue)
				.filter(pathSchemas -> pathSchemas.containsKey(method))
				.map(pathSchemas -> pathSchemas.get(method))
				.findFirst();
	}

	void setSchemas(Map<String, Map<String, String>> schemas) {
		this.schemas = schemas;
	}

	@Value("#{jsonSchemas}")
	private Map<String, Map<String, String>> schemas;
}
