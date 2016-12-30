package io.ari.bucks.domain.repositories.configuration;

import io.ari.money.domain.Money;
import io.ari.repositories.assemblers.JacksonStorageAssembler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StorageAssemblersConfiguration {

	@Bean
	public JacksonStorageAssembler<Money> moneyStorageAssembler() {
		return new JacksonStorageAssembler<Money>(Money.class);
	}
}
