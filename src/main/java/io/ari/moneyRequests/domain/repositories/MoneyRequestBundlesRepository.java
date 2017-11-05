package io.ari.moneyRequests.domain.repositories;

import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.repositories.assemblers.StorageAssembler;
import io.ari.repositories.entities.EntitiesRepository;

public class MoneyRequestBundlesRepository extends EntitiesRepository<MoneyRequestBundle> {

	public MoneyRequestBundlesRepository(StorageAssembler<MoneyRequestBundle> storageAssembler) {
		super(storageAssembler);
	}
/*
	public MoneyRequestBundle findByMoneyRequestId(String moneyRequestId) throws EntityNotFound {
		Map<String, Object> foundMoneyRequestBundleData = getReadRepository()
				.findByKey(ImmutableMap.of("moneyRequests.id", moneyRequestId))
				.orElseThrow(EntityNotFound::new);
		
		return convertDtoToEntity(foundMoneyRequestBundleData);
	}*/
}
