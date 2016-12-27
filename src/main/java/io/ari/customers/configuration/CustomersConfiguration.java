package io.ari.customers.configuration;

import io.ari.repositories.write.EntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomersConfiguration {

	@Bean(name = "customersRepository")
	public EntityRepository getCustomersRepository() {
		return new EntityRepository("subject");
	}
}
