package io.ari.moneyRequests.resources.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.time.TimeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;

@Component
public class MoneyRequestBundlesAssembler {

	public MoneyRequestBundle convertDtoToEntity(String customerId, Map<String, Object> dto) {
		Collection<Map<String, Object>> singleRequestDtos = (Collection<Map<String, Object>>) dto.get("moneyRequests");

		String bucksId = bucksRepository.findBucksByCustomerId(customerId).getId();

        MoneyRequest[] moneyRequests = new MoneyRequest[0];

        if(singleRequestDtos != null) {
            moneyRequests = singleRequestDtos.stream()
                    .map(moneyRequestDto -> moneyRequestsAssembler.convertDtoToEntity(moneyRequestDto, bucksId))
                    .collect(Collectors.toList())
                    .toArray(new MoneyRequest[]{});
        }
		MoneyRequestBundle bundle = new MoneyRequestBundle(UUID.randomUUID().toString(), timeServer.currentDate(), bucksId, moneyRequests);
		bundle.setReason((String) dto.get("reason"));

		return bundle;
	}

	public Map<String, Object> convertEntityToDto(MoneyRequestBundle moneyRequestBundle) {
		Map<String, Object> moneyRequestBundleDto = newHashMap();

		if (moneyRequestBundle.getId() != null) {
			moneyRequestBundleDto.put("id", moneyRequestBundle.getId());
		}

		moneyRequestBundleDto.put("amount", moneyAssembler.convertEntityToDto(moneyRequestBundle.calculateAmount()));
		moneyRequestBundleDto.put("moneyRequests", moneyRequestsAssembler.convertEntitiesToDtos(moneyRequestBundle.getRequests()));
		assignReason(moneyRequestBundle, moneyRequestBundleDto);
		assignErrors(moneyRequestBundle, moneyRequestBundleDto);

		moneyRequestBundleDto.put("_links",
				ImmutableMap.of("self", ImmutableList.of(
						ImmutableMap.of("method", "GET",
								"href", "api/movements/" + moneyRequestBundle.getId(),
								"api", "ari-read"))));

		return ImmutableMap.copyOf(moneyRequestBundleDto);
	}

	private void assignReason(MoneyRequestBundle moneyRequestBundle, Map<String, Object> moneyRequestBundleDto) {
		if (moneyRequestBundle.getReason() != null) {
			moneyRequestBundleDto.put("reason", moneyRequestBundle.getReason());
		}
	}

	private void assignErrors(MoneyRequestBundle moneyRequestBundle, Map<String, Object> moneyRequestBundleDto) {
		if (moneyRequestBundle.hasViolations()) {
			moneyRequestBundleDto.put("status", violationsAssembler.convertEntitiesToDtos(moneyRequestBundle.getViolations()));
		}
	}

	@Autowired
	private BucksRepository bucksRepository;

	@Autowired
	private MoneyRequestsAssembler moneyRequestsAssembler;

	@Autowired
	private ViolationsAssembler violationsAssembler;

	@Autowired
	private MoneyAssembler moneyAssembler;
	
	@Autowired
	private TimeServer timeServer;

}
