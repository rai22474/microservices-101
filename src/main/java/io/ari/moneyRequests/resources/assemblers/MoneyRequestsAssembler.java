package io.ari.moneyRequests.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.RecipientsAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.moneyOrders.domain.businessRules.OrderRecipientIsNotTheSender;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.moneyRequests.domain.MoneyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;

@Component
public class MoneyRequestsAssembler {

	public MoneyRequest convertDtoToEntity(Map<String, Object> requestData, String bucksId) {
		MoneyRequest moneyRequest = new MoneyRequest(UUID.randomUUID().toString());
		moneyRequest.setAmount(moneyAssembler.convertDtoToEntity((Map<String, Object>) requestData.get("amount")));
		moneyRequest.setRecipient(getRecipient(requestData));
		moneyRequest.setBucksId(bucksId);

		return moneyRequest;
	}

	public Collection<Map<String, Object>> convertEntitiesToDtos(Collection<MoneyRequest> moneyRequests) {
		return moneyRequests.stream()
				.map(this::convertEntityToDto)
				.collect(Collectors.toList());
	}

	public Map<String, Object> convertEntityToDto(MoneyRequest moneyRequest) {
		Map<String, Object> moneyRequestDto = newHashMap();

		moneyRequestDto.put("id", moneyRequest.getId());
		moneyRequestDto.put("amount", moneyAssembler.convertEntityToDto(moneyRequest.getAmount()));
		moneyRequestDto.putAll(partiesAssembler.convertEntityToDto(moneyRequest.getRecipient()));
		assignViolations(moneyRequest, moneyRequestDto);

		return ImmutableMap.copyOf(moneyRequestDto);
	}


	private void assignViolations(MoneyRequest moneyRequest, Map<String, Object> moneyRequestDto) {
		Collection<Violation> violations = validator.validate(moneyRequest);
		if (violations.isEmpty()) {
			return;
		}

		Set<Violation> filteredViolations = concat(getUnfilteredViolations(violations).stream(), getOpaqueViolations(violations).stream()).collect(toSet());
		moneyRequestDto.put("status", violationsAssembler.convertEntitiesToDtos(filteredViolations));
	}

	private Set<Violation> getUnfilteredViolations(Collection<Violation> violations) {
		return violations
				.stream()
				.filter(violation -> OrderRecipientIsNotTheSender.ERROR_CODE.equals(violation.getCode()))
				.collect(toSet());
	}

	private Set<Violation> getOpaqueViolations(Collection<Violation> violations) {
		return violations
				.stream()
				.filter(violation -> !OrderRecipientIsNotTheSender.ERROR_CODE.equals(violation.getCode()))
				.map(this::getOpaqueViolation)
				.collect(toSet());
	}

	private Violation getOpaqueViolation(Violation sourceViolation) {
		return new Violation("RecipientCannotReceive", "Recipient cannot receive the money request");
	}

	private Recipient getRecipient(Map<String, Object> orderData) {
		Map<String, Object> recipientData = Maps.filterKeys(orderData, key -> !key.equals("amount") && !key.equals("status"));
		return partiesAssembler.convertDtoToEntity(recipientData);
	}

	@Autowired
	private MoneyAssembler moneyAssembler;

	@Autowired
	private ViolationsAssembler violationsAssembler;

	@Autowired
	private RecipientsAssembler partiesAssembler;

	@Autowired
	@Qualifier("moneyRequestValidator")
	private BusinessRulesValidator<MoneyRequest> validator;

}