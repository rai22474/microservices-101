package io.ari.moneyRequests.domain.repositories.configuration;

import io.ari.moneyRequests.domain.MoneyRequestReception;
import io.ari.repositories.entities.EntitiesRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoneyRequestRepositoriesConfiguration {


	@Bean(name = "moneyRequestReceptionRepository")
	public EntitiesRepository<MoneyRequestReception> moneyRequestReceptionRepository() {
		return new EntitiesRepository<MoneyRequestReception>();
	}
}
