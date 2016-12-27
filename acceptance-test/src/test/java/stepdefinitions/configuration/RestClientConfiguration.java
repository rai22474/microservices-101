package stepdefinitions.configuration;

import io.ari.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class RestClientConfiguration {

	@Bean(name = "readClient")
	public RestClient getReadRestClient() {
		return new RestClient("http://localhost:8080");
	}

}
