package stepdefinitions.configuration;

import io.ari.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfiguration {

	@Bean(name = "readClient")
	public RestClient getReadRestClient() {
		return new RestClient("http://localhost:8080");
	}

}
