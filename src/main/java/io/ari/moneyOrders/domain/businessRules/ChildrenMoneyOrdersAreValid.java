package io.ari.moneyOrders.domain.businessRules;

import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.stream.Collectors.toSet;

@Component
public class ChildrenMoneyOrdersAreValid implements BusinessRule<MoneyOrderBundle> {

	@Override
	public Collection<Violation> isSatisfied(MoneyOrderBundle moneyOrderBundle, Object... additionalData) {
		return moneyOrderBundle.getOrders()
				.stream()
				.filter(moneyOrder -> !validate(moneyOrder).isEmpty())
				.map(this::getViolation)
				.collect(toSet());
	}

	private Violation getViolation(MoneyOrder moneyOrder) {
		return new Violation("InvalidMoneyOrders", "The money orders are not valid");
	}

	private Collection<Violation> validate(MoneyOrder moneyOrder) {
		return businessRulesValidator.validate(moneyOrder);
	}

	@Autowired
	@Qualifier("moneyOrderValidator")
	private BusinessRulesValidator<MoneyOrder> businessRulesValidator;
}
