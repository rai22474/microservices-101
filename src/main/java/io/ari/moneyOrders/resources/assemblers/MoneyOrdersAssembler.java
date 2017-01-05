package io.ari.moneyOrders.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.RecipientsAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.businessRules.OrderRecipientIsNotTheSender;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.uidGenerator.UIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;


@Component
public class MoneyOrdersAssembler {

	public MoneyOrder convertDtoToEntity(Map<String, Object> orderData, String bucksId) {
		MoneyOrder moneyOrder = new MoneyOrder(uidGenerator.generateUID());
		moneyOrder.setAmount(getAmount(orderData));
		moneyOrder.setRecipient(getRecipient(orderData));
		moneyOrder.setBucksId(bucksId);

		return moneyOrder;
	}
	
	public Collection<Map<String, Object>> convertEntitiesToDtos(Collection<MoneyOrder> moneyOrders) {
		return moneyOrders.stream()
				.map(this::convertEntityToDto)
				.collect(toList());
	}

	public Map<String, Object> convertEntityToDto(MoneyOrder moneyOrder) {
		Map<String, Object> moneyOrderDto = newHashMap();

		moneyOrderDto.put("amount", moneyAssembler.convertEntityToDto(moneyOrder.getAmount()));
		moneyOrderDto.put("id", moneyOrder.getId());
		moneyOrderDto.putAll(recipientsAssembler.convertEntityToDto(moneyOrder.getRecipient()));
		assignViolations(moneyOrder, moneyOrderDto);

		return ImmutableMap.copyOf(moneyOrderDto);
	}

	private void assignViolations(MoneyOrder moneyOrder, Map<String, Object> moneyOrderDto) {
		Collection<Violation> violations = businessRulesValidator.validate(moneyOrder);
		if (violations.isEmpty()) {
			return;
		}

		Set<Violation> filteredViolations = concat(getUnfilteredViolations(violations).stream(), getOpaqueViolations(violations)
				.stream())
				.collect(toSet());

		moneyOrderDto.put("status", violationsAssembler.convertEntitiesToDtos(filteredViolations));
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
		return new Violation("RecipientCannotReceive", "Recipient cannot receive the money order");
	}

	private Money getAmount(Map<String, Object> orderData) {
		return moneyAssembler.convertDtoToEntity((Map<String, Object>) orderData.get("amount"));
	}

	private Recipient getRecipient(Map<String, Object> orderData) {
		Map<String, Object> recipientData = Maps.filterKeys(orderData, key -> !key.equals("amount"));
		return recipientsAssembler.convertDtoToEntity(recipientData);
	}

	@Autowired
	private MoneyAssembler moneyAssembler;

	@Autowired
	private ViolationsAssembler violationsAssembler;

	@Autowired
	private RecipientsAssembler recipientsAssembler;
	
	@Autowired
	private UIDGenerator uidGenerator;

	@Autowired
	@Qualifier("moneyOrderValidator")
	private BusinessRulesValidator<MoneyOrder> businessRulesValidator;

}
