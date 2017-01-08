package io.ari.schemaValidations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SchemasContainerTest {

	@Test
	public void shouldNotReturnNull() {
		assertNotNull(schemasContainer.getSchemaLocation(PATH, METHOD));
	}

	@Test
	public void shouldReturnSchemaWhenExists() {

		Optional<String> returnedSchema = schemasContainer.getSchemaLocation(PATH, METHOD);

		assertTrue("Returned schema must be present.", returnedSchema.isPresent());
		assertEquals("Returned schema must be the expected.", SCHEMA, returnedSchema.get());
	}

	@Test
	public void shouldReturnEmptyWhenNoSchemasForPath() {
		Optional<String> returnedSchema = schemasContainer.getSchemaLocation("/non-existing-path", METHOD);

		assertFalse("Returned schema cannot be present.", returnedSchema.isPresent());
	}

	@Test
	public void shouldReturnEmptyWhenNoSchemasForMethod() {
		Optional<String> returnedSchema = schemasContainer.getSchemaLocation(PATH, "DELETE");

		assertFalse("Returned schema cannot be present.", returnedSchema.isPresent());
	}

	@Before
	public void setupContainer() {
		schemasContainer.setSchemas(schemas);
	}

	@InjectMocks
	private SchemasContainer schemasContainer;

	private static final String REGULAR_EXPRESSION_PATH = "existingresource";

	private static final String PATH = "existingresource";

	private static final String METHOD = "POST";

	private static final String SCHEMA = "schemas/schema.json";

	private static final Map<String, Map<String, String>> schemas = of(
            REGULAR_EXPRESSION_PATH, of(
					METHOD, SCHEMA));

}