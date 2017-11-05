package io.ari.moneyOrders.domain.repositories;

import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.repositories.assemblers.StorageAssembler;
import io.ari.repositories.entities.EntitiesRepository;

public class MoneyOrderBundlesRepository extends EntitiesRepository<MoneyOrderBundle> {

	public MoneyOrderBundlesRepository(StorageAssembler<MoneyOrderBundle> storageAssembler) {
		super(storageAssembler);
	}

/*	public MoneyOrderBundle findByMoneyOrderId(String moneyOrderId) throws EntityNotFound {
		Map<String, Object> moneyOrderBundleData = getReadRepository().findByKey(ImmutableMap.of("moneyOrders.id", moneyOrderId)).orElseThrow(EntityNotFound::new);
		return convertDtoToEntity(moneyOrderBundleData);
	}*/
}
