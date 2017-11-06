package io.ari.bucks.domain.repositories;

import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.Bucks;
import io.ari.repositories.entities.EntitiesRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BucksRepository extends EntitiesRepository<Bucks> {

	public Bucks findByCustomerId(String customerId) {
		return super.findById(customerBucks.get(customerId));
	}

	public Bucks findBucksByCustomerId(String customerId){
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

	private Map<String, Object> bucksByBankingServiceIdQuery(String bankingServiceId) {
		return ImmutableMap.of("bankingServiceAgreementId", bankingServiceId, "type", "bucks");
	}

	private IllegalStateException throwNoBucksException() {
		return new IllegalStateException("The system is not consistent. All the customers must have an assigned bucks");
	}

	private Map<String,String> customerBucks = new HashMap<>();

}
