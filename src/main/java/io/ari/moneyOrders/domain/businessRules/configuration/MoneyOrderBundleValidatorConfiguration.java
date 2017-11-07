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
public class MoneyOrderBundleValidatorConfiguration {

	@Bean(name = "moneyOrderBundleValidator")
	public BusinessRulesValidator<MoneyOrderBundle> moneyOrderBundleValidator() {
		return new BusinessRulesValidator<>(ImmutableSet.of(
				sourceBucksCanWithdraw,
				childrenMoneyOrdersAreValid));
	}

	@Autowired
	private SourceBucksCanWithdraw sourceBucksCanWithdraw;

	@Autowired
	private ChildrenMoneyOrdersAreValid childrenMoneyOrdersAreValid;

}
