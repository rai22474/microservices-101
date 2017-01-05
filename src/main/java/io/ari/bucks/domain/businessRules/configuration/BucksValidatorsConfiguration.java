package io.ari.bucks.domain.businessRules.configuration;

import com.google.common.collect.ImmutableSet;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.businessRules.BucksHaveEnoughAvailable;
import io.ari.bucks.domain.businessRules.BucksHaveEnoughRemainingRechargeLimit;
import io.ari.bucks.domain.businessRules.TvaIsEnabled;
import io.ari.bussinessRules.BusinessRulesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BucksValidatorsConfiguration {

	@Bean(name = "bucksWithdrawalValidator")
	public BusinessRulesValidator<Bucks> bucksWithdrawalValidator() {
		return new BusinessRulesValidator<>(ImmutableSet.of(
				bucksHaveEnoughAvailable,
				tvaIsEnabled));
	}

	@Bean(name = "bucksReceptionValidator")
	public BusinessRulesValidator<Bucks> bucksReceptionValidator() {
		return new BusinessRulesValidator<>(ImmutableSet.of(
				bucksHaveEnoughRemainingRechargeLimit,
				tvaIsEnabled));
	}

	@Autowired
	private BucksHaveEnoughAvailable bucksHaveEnoughAvailable;

	@Autowired
	private BucksHaveEnoughRemainingRechargeLimit bucksHaveEnoughRemainingRechargeLimit;

	@Autowired
	private TvaIsEnabled tvaIsEnabled;

}
