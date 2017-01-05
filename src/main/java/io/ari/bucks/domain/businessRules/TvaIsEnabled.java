package io.ari.bucks.domain.businessRules;

import com.google.common.collect.ImmutableSet;
import io.ari.bucks.domain.Bucks;
import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.Violation;
import io.ari.cards.domain.Card;
import io.ari.cards.domain.repositories.CardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class TvaIsEnabled implements BusinessRule<Bucks> {

	@Override
	public Collection<Violation> isSatisfied(Bucks bucks, Object... additionalData) {

		if (getTva(bucks).isBlocked()) {
			return ImmutableSet.of(new Violation("BlockedBucks", "The tva is blocked"));
		}

		return ImmutableSet.of();
	}

	private Card getTva(Bucks bucks) {
		return cardsRepository.findByCustomerIdAndType(bucks.getCustomerId(), "tva");
	}

	@Autowired
	private CardsRepository cardsRepository;

}
