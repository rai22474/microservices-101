package io.ari.bucks.domain.repositories.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.Bucks;
import io.ari.money.domain.Money;
import io.ari.repositories.assemblers.StorageAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Component
public class BucksStorageAssembler extends StorageAssembler<Bucks> {

	public Map<String, Object> convertEntityToDto(Bucks bucks) {
		Map<String, Object> bucksDto = newHashMap();

		bucksDto.put("balance", createBucksBalance(bucks));
		bucksDto.put("limits", createBucksLimits(bucks));
		bucksDto.put("blockedBalances", createBlockedBalances(bucks));
		bucksDto.put("bankingServiceAgreementId", bucks.getBankingServiceAgreementId());
		bucksDto.put("participants", ImmutableList.of(ImmutableMap.of("id", bucks.getCustomerId())));
		bucksDto.put("type", "bucks");

		return ImmutableMap.copyOf(bucksDto);
	}

	public Bucks convertDtoToEntity(Map<String, Object> bucksData) {
		String customerId = (String) ((List<Map<String, Object>>) bucksData.get("participants")).stream().findFirst().get().get("id");
		Bucks bucks = new Bucks(customerId, (String) bucksData.get("bankingServiceAgreementId"));

		assignBalances(bucksData, bucks);
		assignLimits(bucksData, bucks);

		assignBlockedBalances(bucksData, bucks);
		
		return bucks;
	}

	private void assignBlockedBalances(Map<String, Object> bucksData, Bucks bucks) {
		Map<String, Object> blockedBalances = (Map<String, Object>) bucksData.get("blockedBalances");

		blockedBalances.forEach(
				(blockedBalanceId, money) -> bucks.addBlockedBalances(blockedBalanceId,
						moneyStorageAssembler.convertDtoToEntity((Map<String, Object>) money)));
	}

	private void assignBalances(Map<String, Object> bucksData, Bucks bucks) {
		Map<String, Object> balance = (Map<String, Object>) bucksData.get("balance");
		bucks.setTotalBalance(moneyStorageAssembler.convertDtoToEntity((Map<String, Object>) balance.get("total")));
	}

	private void assignLimits(Map<String, Object> bucksData, Bucks bucks) {
		Map<String, Object> limits = (Map<String, Object>) bucksData.get("limits");

		Map<String, Object> recharge = (Map<String, Object>) limits.get("recharge");
		bucks.setMaxRechargeLimit(moneyStorageAssembler.convertDtoToEntity((Map<String, Object>) recharge.get("max")));
		bucks.setLastRecharge(moneyStorageAssembler.convertDtoToEntity((Map<String, Object>) recharge.get("last")));
		bucks.setThisPeriodRechargeLimit(moneyStorageAssembler.convertDtoToEntity((Map<String, Object>) recharge.get("thisPeriod")));
		bucks.setDaysTillMaxLimit((Integer) limits.get("daysTillMaxLimit"));
	}

	private Map<String, Object> createBlockedBalances(Bucks bucks) {
		Map<String, Object> blockedBalancesDto = newHashMap();
		bucks.getBlockedBalances().
				forEach((id, money) -> blockedBalancesDto.put(id, moneyStorageAssembler.convertEntityToDto(money)));
	
		return ImmutableMap.copyOf(blockedBalancesDto);
	}

	private Map<String, Object> createBucksLimits(Bucks bucks) {
		return ImmutableMap.of(
				"recharge", ImmutableMap.of(
						"remaining", moneyStorageAssembler.convertEntityToDto(bucks.getRemainingRechargeLimit()),
						"last", moneyStorageAssembler.convertEntityToDto(bucks.getLastRecharge()),
						"max", moneyStorageAssembler.convertEntityToDto(bucks.getMaxRechargeLimit()),
						"thisPeriod", moneyStorageAssembler.convertEntityToDto(bucks.getThisPeriodRechargeLimit())),
					"daysTillMaxLimit", bucks.getDaysTillMaxLimit());
	}

	private Map<String, Object> createBucksBalance(Bucks bucks) {
		return ImmutableMap.of(
				"available", moneyStorageAssembler.convertEntityToDto(bucks.getAvailableBalance()),
				"blocked", moneyStorageAssembler.convertEntityToDto(bucks.getBlockedBalance()),
				"total", moneyStorageAssembler.convertEntityToDto(bucks.getTotalBalance()));
	}

	@Autowired
	@Qualifier("moneyStorageAssembler")
	private StorageAssembler<Money> moneyStorageAssembler;
}
