package io.ari.cards.domain.repositories.configuration;

import io.ari.cards.domain.Card;
import io.ari.repositories.assemblers.JacksonStorageAssembler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardRepositoriesConfiguration {

	@Bean(name = "cardsStorageAssemblerJackson")
	public JacksonStorageAssembler<Card> cardsStorageAssembler() {
		return new JacksonStorageAssembler<>(Card.class);
	}
}
