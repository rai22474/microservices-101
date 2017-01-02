package io.ari.cards.domain.businessRules;

import com.google.common.collect.ImmutableSet;
import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.Violation;
import io.ari.cards.domain.Card;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class IsActiveCard implements BusinessRule<Card> {

	@Override
	public Collection<Violation> isSatisfied(Card card, Object... additionalData) {
		if (!card.isActive()) {
			return ImmutableSet.of(getViolation());
		}

		return ImmutableSet.of();
	}

	private Violation getViolation() {
		return new Violation("CardAlreadyBlocked", "The card is not active");
	}

}
