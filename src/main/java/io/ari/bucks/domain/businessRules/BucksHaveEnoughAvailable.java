package io.ari.bucks.domain.businessRules;

import com.google.common.collect.ImmutableSet;
import io.ari.bucks.domain.Bucks;
import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class BucksHaveEnoughAvailable implements BusinessRule<Bucks> {

	@Override
	public Collection<Violation> isSatisfied(Bucks bucks, Object... additionalData) {
		Money amount = (Money) additionalData[0];
		return bucks.getAvailableBalance().isEnoughFor(amount) ? ImmutableSet.of() : ImmutableSet.of(getViolation());
	}

	private Violation getViolation() {
		return new Violation("NotEnoughAvailable", "Bucks doesn't have enough available");
	}

}
