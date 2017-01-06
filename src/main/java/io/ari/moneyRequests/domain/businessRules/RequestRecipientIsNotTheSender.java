package io.ari.moneyRequests.domain.businessRules;

import com.google.common.collect.ImmutableSet;
import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.Violation;
import io.ari.moneyRequests.domain.MoneyRequest;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RequestRecipientIsNotTheSender implements BusinessRule<MoneyRequest> {

	@Override
	public Collection<Violation> isSatisfied(MoneyRequest moneyRequest, Object... additionalData) {

		if (moneyRequest.getRecipient().isTheSameAs(moneyRequest.getBucksId()))
			return ImmutableSet.of(getViolation());

		return ImmutableSet.of();
	}

	private Violation getViolation() {
		return new Violation(ERROR_CODE, "The recipient is the same as the sender.");
	}

	public static final String ERROR_CODE = "SameRecipientAsSender";

}
