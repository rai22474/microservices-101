package io.ari.repositories.assemblers;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.ari.assemblers.converters.ISOStringToDateValueConverter;
import org.junit.Before;

/**
 * Abstract unit test for JacksonStorageAssembler subclasses.
 */
public abstract class JacksonStorageAssemblerTest {

	@Before
	public void setupAssemblerObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		SimpleModule module = new SimpleModule("DateDeserializerModule",
				new Version(1, 0, 0, null, null, null));

		module.addDeserializer(String.class, new ISOStringToDateValueConverter());
		mapper.registerModule(module);

		getAssembler().setObjectMapper(mapper);
	}

	protected abstract JacksonStorageAssembler<?> getAssembler();
}