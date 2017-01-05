package io.ari.moneyOrders.domain.recipients.businessRules;

import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.recipients.BucksRecipient;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class BucksFromRecipientCanReceive implements BusinessRule<Recipient> {

	@Override
	public Collection<Violation> isSatisfied(Recipient recipient, Object... additionalData) {
		if (recipient instanceof BucksRecipient) {
			Money amount = (Money) additionalData[0];

			return bucksReceptionValidator.validate(getTargetBucks(((BucksRecipient)recipient).getTargetBucksId()), amount);
		}

		return new ArrayList<>();
	}

	private Bucks getTargetBucks(String targetBucksId) {
		try {
			return bucksRepository.findById(targetBucksId);
		} catch (EntityNotFound e) {
			throw new IllegalStateException(e);
		}
	}

	@Autowired
	@Qualifier("bucksReceptionValidator")
	private BusinessRulesValidator<Bucks> bucksReceptionValidator;

	@Autowired
	private BucksRepository bucksRepository;

}
