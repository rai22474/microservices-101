package io.ari.moneyOrders.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.repositories.MoneyOrderBundlesRepository;
import io.ari.repositories.entities.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;


@Configurable(dependencyCheck = true)
public class MoneyOrderBundle implements Entity{

	public MoneyOrderBundle(String id,
							Date creationDate,
							String bucksId,
							Collection<MoneyOrder> moneyOrders) {
		this.bucksId = bucksId;
		this.orders = moneyOrders;
		this.id = id;
		this.creationDate = creationDate;
	}

	public MoneyOrderBundle(String id,
							Date creationDate,
							String bucksId,
							MoneyOrder... moneyOrders) {
		this.bucksId = bucksId;
        this.orders = Arrays.asList(moneyOrders);
        this.id = id;
		this.creationDate = creationDate;
	}

	public Money calculateAmount() {
		return orders.stream()
				.map(MoneyOrder::getAmount)
				.reduce(new Money(BigDecimal.ZERO, "EUR"), (first, second) -> first.add(second));
	}

	/*
	public void submit() {
		orders.stream().forEach(
				order -> {
					Future<String> submissionFuture = future(order::submit, actorSystem.dispatcher());
					submissionFuture.onSuccess(successCallback(moneyOrderSubmitted(order.getId(), id, moneyOrderBundlesRepository)),
							actorSystem.dispatcher());
				});
	}

	public void processTransferEvent(String moneyOrderId, Map<String, Object> event) {
		MoneyOrder moneyOrder = findMoneyOrderById(moneyOrderId);
		moneyOrder.processTransferEvent(event);
		eventsPublisher.publish(movementsFactory.createFact(moneyOrder.getAmount(),
				bucksId,
				(String) moneyOrder.getRecipient().getData().get("targetBucksId"),
				"moneyOrder",
				reason,
				id));
	}
	

	private Consumer<String> moneyOrderSubmitted(String moneyOrderId, String moneyOrderBundleId, MoneyOrderBundlesRepository moneyOrderBundlesRepository) {
		return transactionId -> {
			future(() -> MoneyOrderBundle.changeStatusToSubmitted(moneyOrderId, moneyOrderBundleId, moneyOrderBundlesRepository), actorSystem.dispatcher());
		};
	}

	private static MoneyOrderBundle changeStatusToSubmitted(String moneyOrderId, String moneyOrderBundleId, MoneyOrderBundlesRepository moneyOrderBundlesRepository) {
		MoneyOrderBundle moneyOrderBundle = findMoneyOrderBundle(moneyOrderBundleId, moneyOrderBundlesRepository);
		moneyOrderBundle.findMoneyOrderById(moneyOrderId).changeStatus("submitted");
		return moneyOrderBundlesRepository.update(moneyOrderBundleId, moneyOrderBundle);
	}

	private static MoneyOrderBundle findMoneyOrderBundle(String id, MoneyOrderBundlesRepository repository) {
		try {
			return repository.findById(id);
		} catch (EntityNotFound entityNotFound) {
			throw new IllegalArgumentException();
		}
	}

	private MoneyOrder findMoneyOrderById(String moneyOrderId) {
		return orders.stream().
				filter(moneyOrder -> moneyOrder.getId().equals(moneyOrderId)).findFirst().get();
	}
*/

	public Collection<MoneyOrder> getOrders() {
		return ImmutableList.copyOf(orders);
	}

	public Collection<Violation> getViolations() {
		return violations;
	}

	public boolean hasViolations() {
		return !violations.isEmpty();
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBucksId() {
		return bucksId;
	}

	void setMoneyOrderBundlesRepository(MoneyOrderBundlesRepository moneyOrderBundlesRepository) {
		this.moneyOrderBundlesRepository = moneyOrderBundlesRepository;
	}

	/*
	void setEventsPublisher(EventsPublisher eventsRepository) {
		this.eventsPublisher = eventsRepository;
	}

	void setMovementsFactory(FactsFactory movementsFactory) {
		this.movementsFactory = movementsFactory;
	}
*/

	public void setViolations(Collection<Violation> violations) {
		this.violations = violations;
	}

	public String getSourceCommand() {
		return sourceCommand;
	}

	public void setSourceCommand(String sourceCommand) {
		this.sourceCommand = sourceCommand;
	}

	@Autowired
	private MoneyOrderBundlesRepository moneyOrderBundlesRepository;

	//@Autowired
	//private EventsPublisher eventsPublisher;

	//@Autowired
	//private FactsFactory movementsFactory;


	private Collection<MoneyOrder> orders;

	private Collection<Violation> violations = ImmutableSet.of();

	private String reason;

	private String bucksId;

	private String id;

	private String status = "ready";

	private Date creationDate;

	private String sourceCommand;
}

