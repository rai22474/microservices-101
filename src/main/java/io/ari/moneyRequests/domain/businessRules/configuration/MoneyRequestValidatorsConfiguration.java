package io.ari.moneyRequests.domain.businessRules.configuration;

import com.google.common.collect.ImmutableSet;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.moneyRequests.domain.businessRules.ChildrenMoneyRequestsAreValid;
import io.ari.moneyRequests.domain.businessRules.RequestRecipientIsNotTheSender;
import io.ari.moneyRequests.domain.businessRules.RequestSenderCanReceive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoneyRequestValidatorsConfiguration {

	@Bean(name = "moneyRequestBundleValidator")
	public BusinessRulesValidator<MoneyRequestBundle> moneyRequestBundleBusinessRulesValidator() {
		return new BusinessRulesValidator<>(ImmutableSet.of(
				requestSenderCanReceive,
				childrenMoneyRequestsAreValid));
	}

	@Bean(name = "moneyRequestValidator")
	public BusinessRulesValidator<MoneyRequest> moneyRequestValidator() {
		return new BusinessRulesValidator<>(ImmutableSet.of(
				requestRecipientIsNotTheSender));
	}

	@Autowired
	private RequestSenderCanReceive requestSenderCanReceive;

	@Autowired
	private ChildrenMoneyRequestsAreValid childrenMoneyRequestsAreValid;

	@Autowired
	private RequestRecipientIsNotTheSender requestRecipientIsNotTheSender;

}
