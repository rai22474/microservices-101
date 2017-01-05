package io.ari.configuration;

import io.ari.uidGenerator.UIDGenerator;
import io.ari.uidGenerator.java.JavaUIDGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UIDGeneratorConfiguration {

	@Bean
	public UIDGenerator uidGenerator(){
		return new JavaUIDGenerator();
	}
}
