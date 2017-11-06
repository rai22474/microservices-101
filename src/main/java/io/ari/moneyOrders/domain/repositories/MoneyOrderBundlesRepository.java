package io.ari.moneyOrders.domain.repositories;

import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.repositories.entities.EntitiesRepository;
import org.springframework.stereotype.Component;

@Component
public class MoneyOrderBundlesRepository extends EntitiesRepository<MoneyOrderBundle> {

/*	public MoneyOrderBundle findByMoneyOrderId(String moneyOrderId) throws EntityNotFound {
		Map<String, Object> moneyOrderBundleData = getReadRepository().findByKey(ImmutableMap.of("moneyOrders.id", moneyOrderId)).orElseThrow(EntityNotFound::new);
		return convertDtoToEntity(moneyOrderBundleData);
	}*/
}
