package io.ari.moneyRequests.domain.repositories.configuration;

import io.ari.moneyRequests.domain.MoneyRequestReception;
import io.ari.moneyRequests.domain.repositories.MoneyRequestBundlesRepository;
import io.ari.moneyRequests.domain.repositories.assemblers.MoneyRequestBundlesStorageAssembler;
import io.ari.repositories.assemblers.JacksonStorageAssembler;
import io.ari.repositories.assemblers.StorageAssembler;
import io.ari.repositories.entities.EntitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoneyRequestRepositoriesConfiguration {

	@Bean
	public MoneyRequestBundlesRepository getMoneyRequestBundlesRepository() {
		return new MoneyRequestBundlesRepository(storageAssembler);
	}
	
	@Bean(name = "moneyRequestReceptionRepository")
	public EntitiesRepository<MoneyRequestReception> moneyRequestReceptionRepository() {
		return new EntitiesRepository<>(moneyRequestReceptionAssembler());
	}

	@Bean(name = "moneyRequestReceptionAssembler")
	public StorageAssembler<MoneyRequestReception> moneyRequestReceptionAssembler() {
		return new JacksonStorageAssembler<>(MoneyRequestReception.class);
	}

	@Autowired
	private MoneyRequestBundlesStorageAssembler storageAssembler;
}
