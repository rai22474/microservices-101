package io.ari.cards.domain.businessRules;

import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.Violation;
import io.ari.cards.domain.Card;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static com.google.common.collect.ImmutableSet.of;

@Component
public class IsTvaCard implements BusinessRule<Card> {

	@Override
	public Collection<Violation> isSatisfied(Card card, Object... additionalData) {
		if (!card.isTva()) {
			return of(getViolation());
		}

		return of();
	}

	private Violation getViolation() {
		return new Violation("NoValidCardForBlocking", "The card is not a tva.");
	}

}
