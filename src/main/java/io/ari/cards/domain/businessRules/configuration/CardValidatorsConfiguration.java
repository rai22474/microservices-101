package io.ari.cards.domain.businessRules.configuration;

import com.google.common.collect.ImmutableSet;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.cards.domain.Card;
import io.ari.cards.domain.businessRules.IsActiveCard;
import io.ari.cards.domain.businessRules.IsTvaCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardValidatorsConfiguration {

	@Bean(name = "cardBlockValidator")
	public BusinessRulesValidator<Card> cardBlockValidator() {
		return new BusinessRulesValidator<>(ImmutableSet.of(
				isTvaCard,
				isActiveCard)
        );
	}

	@Autowired
	private IsTvaCard isTvaCard;

	@Autowired
	private IsActiveCard isActiveCard;

}
