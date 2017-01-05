package io.ari.moneyOrders.domain.businessRules;

import com.google.common.collect.ImmutableSet;
import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.Violation;
import io.ari.moneyOrders.domain.MoneyOrder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class OrderRecipientIsNotTheSender implements BusinessRule<MoneyOrder> {

	@Override
	public Collection<Violation> isSatisfied(MoneyOrder moneyOrder, Object... additionalData) {

		if (moneyOrder.getRecipient().isTheSameAs(moneyOrder.getBucksId()))
			return ImmutableSet.of(getViolation());

		return ImmutableSet.of();
	}

	private Violation getViolation() {
		return new Violation(ERROR_CODE, "The recipient is the same as the sender.");
	}

	public static final String ERROR_CODE = "SameRecipientAsSender";

}
