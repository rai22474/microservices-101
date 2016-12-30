package io.ari.customers.domain.repositories.configuration;


import io.ari.customers.domain.Customer;
import io.ari.repositories.assemblers.JacksonStorageAssembler;
import io.ari.repositories.assemblers.StorageAssembler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerRepositoriesConfiguration {

	@Bean
	public StorageAssembler<Customer> customersStorageAssembler() {
		return new JacksonStorageAssembler<>(Customer.class);
	}


}

