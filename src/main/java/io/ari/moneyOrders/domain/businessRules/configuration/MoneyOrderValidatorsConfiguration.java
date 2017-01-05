package io.ari.moneyOrders.domain.businessRules.configuration;

import com.google.common.collect.ImmutableSet;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.moneyOrders.domain.businessRules.ChildrenMoneyOrdersAreValid;
import io.ari.moneyOrders.domain.businessRules.OrderRecipientIsNotTheSender;
import io.ari.moneyOrders.domain.businessRules.RecipientCanReceive;
import io.ari.moneyOrders.domain.businessRules.SourceBucksCanWithdraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoneyOrderValidatorsConfiguration {

	@Bean(name = "moneyOrderBundleValidator")
	public BusinessRulesValidator<MoneyOrderBundle> moneyOrderBundleValidator() {
		return new BusinessRulesValidator<>(ImmutableSet.of(
				sourceBucksCanWithdraw,
				childrenMoneyOrdersAreValid));
	}

	@Bean(name = "moneyOrderValidator")
	public BusinessRulesValidator<MoneyOrder> moneyOrderValidator() {
		return new BusinessRulesValidator<>(ImmutableSet.of(
				recipientCanReceive,
				orderRecipientIsNotTheSender));
	}

	@Autowired
	private SourceBucksCanWithdraw sourceBucksCanWithdraw;

	@Autowired
	private ChildrenMoneyOrdersAreValid childrenMoneyOrdersAreValid;

	@Autowired
	private RecipientCanReceive recipientCanReceive;

	@Autowired
	private OrderRecipientIsNotTheSender orderRecipientIsNotTheSender;

}
