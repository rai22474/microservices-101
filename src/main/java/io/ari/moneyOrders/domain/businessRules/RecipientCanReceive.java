package io.ari.moneyOrders.domain.businessRules;

import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.recipients.Recipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RecipientCanReceive implements BusinessRule<MoneyOrder> {

	@Override
	public Collection<Violation> isSatisfied(MoneyOrder moneyOrder, Object... additionalData) {
		Money amount = moneyOrder.getAmount();

		return businessRulesValidator.validate(moneyOrder.getRecipient(),amount);
	}

	@Autowired
	@Qualifier("bucksRecipientValidator")
	private BusinessRulesValidator<Recipient> businessRulesValidator;

}