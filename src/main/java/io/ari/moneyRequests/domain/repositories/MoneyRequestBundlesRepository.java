package io.ari.moneyRequests.domain.repositories;

import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.repositories.entities.EntitiesRepository;
import org.springframework.stereotype.Component;

@Component
public class MoneyRequestBundlesRepository extends EntitiesRepository<MoneyRequestBundle> {

/*
	public MoneyRequestBundle findByMoneyRequestId(String moneyRequestId) throws EntityNotFound {
		Map<String, Object> foundMoneyRequestBundleData = getReadRepository()
				.findByKey(ImmutableMap.of("moneyRequests.id", moneyRequestId))
				.orElseThrow(EntityNotFound::new);
		
		return convertDtoToEntity(foundMoneyRequestBundleData);
	}*/
}
