package io.ari.moneyOrders.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.repositories.exceptions.EntityNotFound;
import io.ari.time.TimeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;


@Component
public class MoneyOrderBundlesAssembler {

	public MoneyOrderBundle convertDtoToEntity(String customerId, Map<String, Object> dto) throws EntityNotFound {
		return createMoneyOrderBundle(customerId, UUID.randomUUID().toString(), dto);
	}

	public MoneyOrderBundle convertDtoToExistingEntity(String customerId, String id, Map<String, Object> dto) throws EntityNotFound {
		return createMoneyOrderBundle(customerId, id, dto);
	}

	public Map<String, Object> convertEntityToDto(MoneyOrderBundle moneyOrderBundle) {
		Map<String, Object> moneyOrderBundleDto = newHashMap();

		moneyOrderBundleDto.put("id", moneyOrderBundle.getId());
		moneyOrderBundleDto.put("amount", moneyAssembler.convertEntityToDto(moneyOrderBundle.calculateAmount()));
		moneyOrderBundleDto.put("moneyOrders", moneyOrdersAssembler.convertEntitiesToDtos(moneyOrderBundle.getOrders()));
		assignReasonToDto(moneyOrderBundle, moneyOrderBundleDto);
		assignErrors(moneyOrderBundle, moneyOrderBundleDto);

		moneyOrderBundleDto.put("_links", hypermediaAssembler.createHypermedia("api/commands/" + moneyOrderBundle.getId(), "wizzo-read"));

		return ImmutableMap.copyOf(moneyOrderBundleDto);
	}

	private void assignErrors(MoneyOrderBundle moneyOrderBundle, Map<String, Object> moneyOrderBundleDto) {
		if (moneyOrderBundle.hasViolations()) {
			moneyOrderBundleDto.put("status", violationsAssembler.convertEntitiesToDtos(moneyOrderBundle.getViolations()));
		}
	}

	private MoneyOrderBundle createMoneyOrderBundle(String customerId, String id, Map<String, Object> dto) throws EntityNotFound {
		Collection<Map<String, Object>> singleOrderDtos = (Collection<Map<String, Object>>) dto.get("moneyOrders");
		Bucks customerBucks = getBucks(customerId);
        MoneyOrder[] moneyOrders = new MoneyOrder[0];

        if (singleOrderDtos != null) {
            moneyOrders = singleOrderDtos.stream()
                    .map(moneyOrderDto -> moneyOrdersAssembler.convertDtoToEntity(moneyOrderDto, customerBucks.getId()))
                    .collect(Collectors.toList())
                    .toArray(new MoneyOrder[]{});
        }
        
		MoneyOrderBundle bundle = new MoneyOrderBundle(id, timeServer.currentDate(), customerBucks.getId(), moneyOrders);
		bundle.setReason((String) dto.get("reason"));

		return bundle;
	}

	private Bucks getBucks(String customerId) throws EntityNotFound {
		return bucksRepository.findBucksByCustomerId(customerId);
	}

	private void assignReasonToDto(MoneyOrderBundle entity, Map<String, Object> moneyOrderBundleDto) {
		if (entity.getReason() != null) {
			moneyOrderBundleDto.put("reason", entity.getReason());
		}
	}

	@Autowired
	private BucksRepository bucksRepository;

	@Autowired
	private MoneyOrdersAssembler moneyOrdersAssembler;

	@Autowired
	private ViolationsAssembler violationsAssembler;

	@Autowired
	private MoneyAssembler moneyAssembler;

	@Autowired
	private HypermediaAssembler hypermediaAssembler;
	
	@Autowired
	private TimeServer timeServer;
}
