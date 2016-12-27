package io.ari.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Configuration
public class ToolbeltConfiguration {

	@Bean(name = "jsonObjectMapper")
	public ObjectMapper jsonObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		mapper.enable(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return mapper;
	}

	@Bean
	public Client client() {
		Client client = ClientBuilder.newClient();
		client.register(new JacksonJsonProvider(jsonObjectMapper()));
		return client;
	}

}
