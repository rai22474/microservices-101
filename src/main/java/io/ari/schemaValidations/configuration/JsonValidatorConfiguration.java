package io.ari.schemaValidations.configuration;

import com.google.common.collect.ImmutableMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JsonValidatorConfiguration {

	@Bean(name = "jsonSchemas")
	public Map<String, Map<String, String>> jsonSchemas() {

        Map<String, Map<String,String>> schemas = new HashMap<>();

        schemas.put("moneyOrders", ImmutableMap.of("POST", "/schemas/moneyOrdersPost.json"));
        schemas.put("drafts/moneyOrders", ImmutableMap.of("POST", "/schemas/moneyOrdersDraftPost.json"));
        schemas.put("^drafts/moneyOrders/.*", ImmutableMap.of("PUT", "/schemas/moneyOrdersDraftPut.json"));
		schemas.put("moneyRequests", ImmutableMap.of("POST", "/schemas/moneyRequestsPost.json"));
		schemas.put("drafts/moneyRequests", ImmutableMap.of("POST", "/schemas/moneyRequestsDraftPost.json"));
		schemas.put("^drafts/moneyRequests/.+", ImmutableMap.of("PUT", "/schemas/moneyRequestsDraftPost.json"));
		schemas.put("customers", ImmutableMap.of("POST", "/schemas/customerPost.json"));
		schemas.put("me", ImmutableMap.of("PUT", "/schemas/mePut.json"));
		schemas.put("withdrawals", ImmutableMap.of("POST", "/schemas/withdrawalsPost.json"));

		return schemas;
	}

}
