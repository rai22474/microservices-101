package io.ari.subjects.configuration;

import io.ari.repositories.write.EntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubjectsConfiguration {

	@Bean(name = "subjectsRepository")
	public EntityRepository getSubjectsRepository() {
		return new EntityRepository("subject");
	}
}
