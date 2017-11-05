package io.ari.bucks.domain.repositories;

import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.assemblers.BucksStorageAssembler;
import io.ari.repositories.entities.EntitiesRepository;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BucksRepository extends EntitiesRepository<Bucks> {

	@Autowired
	public BucksRepository(BucksStorageAssembler storageAssembler) {
		super(storageAssembler);
	}

	public Map<String,Object> findByCustomerId(String customerId) throws EntityNotFound {
		return super.findOne(customerBucks.get(customerId));
	}

	public Bucks findBucksByCustomerId(String customerId) throws EntityNotFound {
		return super.findById(customerBucks.get(customerId));
	}

	public Bucks save (Bucks bucks){
		customerBucks.put(bucks.getCustomerId(),bucks.getId());
		return super.save(bucks);
	}

	public void deleteCustomer(String customerId) {

		/*	Bucks bucks = findByCustomerId(customerId);
		bankingService.deleteAgreement(bucks.getBankingServiceAgreementId());
		getWriteRepository().delete(bucksByCustomerFilter(customerId));
		return bucks;*/
	}

	public Bucks findByBankingServiceId(String bankingServiceId) throws EntityNotFound {
		/*Map<String, Object> query = bucksByBankingServiceIdQuery(bankingServiceId);
		Map<String, Object> foundBucksData = getReadRepository().findByKey(query).orElseThrow(EntityNotFound::new);
		return convertDtoToEntity(foundBucksData);*/
		return null;
	}

	private Map<String, Object> bucksByCustomerQuery(String customerId) {
		return ImmutableMap.of("participants.id", customerId, "type", "bucks");
	}

	/*
	private EntityFilter bucksByCustomerFilter(String customerId) {
		EntityFilter entityFilter = new EntityFilter();
		
		entityFilter.addPredicate(new EqualsPredicate("participants.id", customerId));
		entityFilter.addPredicate(new EqualsPredicate("type", "bucks"));
		
		return entityFilter;
	}
*/
	private Map<String, Object> bucksByBankingServiceIdQuery(String bankingServiceId) {
		return ImmutableMap.of("bankingServiceAgreementId", bankingServiceId, "type", "bucks");
	}

	private IllegalStateException throwNoBucksException() {
		return new IllegalStateException("The system is not consistent. All the customers must have an assigned bucks");
	}

	private Map<String,String> customerBucks = new HashMap<>();

//	@Autowired
//	private BankingService bankingService;
}
