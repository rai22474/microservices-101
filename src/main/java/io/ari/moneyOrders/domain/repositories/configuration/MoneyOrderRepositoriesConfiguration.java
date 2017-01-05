package io.ari.moneyOrders.domain.repositories.configuration;

import io.ari.moneyOrders.domain.repositories.MoneyOrderBundlesRepository;
import io.ari.moneyOrders.domain.repositories.assemblers.MoneyOrderBundlesStorageAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoneyOrderRepositoriesConfiguration {

	@Bean
	public MoneyOrderBundlesRepository getMoneyOrderBundlesRepository() {
		return new MoneyOrderBundlesRepository(storageAssembler);
	}

	@Autowired
	private MoneyOrderBundlesStorageAssembler storageAssembler;
}
