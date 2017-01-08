package io.ari.moneyRequests.domain;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyRequests.domain.repositories.MoneyRequestBundlesRepository;
import io.ari.repositories.entities.Entity;
import io.ari.repositories.exceptions.EntityNotFound;
import io.ari.time.TimeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Configurable(dependencyCheck = true)
public class MoneyRequestBundle implements Entity{

	public MoneyRequestBundle(String id, Date creationDate, String bucksId, MoneyRequest... moneyRequests) {
		this.bucksId = bucksId;
		this.requests = Arrays.asList(moneyRequests);
		this.id = id;
		this.creationDate = creationDate;
	}

	public Money calculateAmount() {
		return requests.stream()
				.map(MoneyRequest::getAmount)
				.reduce(new Money(BigDecimal.ZERO, "EUR"), (first, second) -> first.add(second));
	}

	public MoneyRequestBundle clone() {
		List<MoneyRequest> clonedRequests = requests.stream()
				.map(MoneyRequest::clone)
				.collect(Collectors.toList());

		MoneyRequestBundle moneyRequestBundle = new MoneyRequestBundle(UUID.randomUUID().toString(),
				timeServer.currentDate(),
				this.bucksId,
				clonedRequests.toArray(new MoneyRequest[]{}));
		
		moneyRequestBundle.setReason(getReason());
		moneyRequestBundle.setSourceCommand(getId());
		
		return moneyRequestBundle;
	}


	public void submit() {
		/*requests.stream().forEach(
				request -> {
					Future<String> submissionFuture = future(() -> request.submit(getBucksId(), reason), actorSystem.dispatcher());
					submissionFuture.onSuccess(successCallback(moneyRequestSubmitted(request.getId(), id, moneyRequestBundlesRepository)),
							actorSystem.dispatcher());
				});*/
	}
/*
	private Consumer<String> moneyRequestSubmitted(String moneyRequestId,
												   String moneyRequestBundleId,
												   MoneyRequestBundlesRepository moneyRequestBundlesRepository) {
		return transactionId -> {
			future(() -> MoneyRequestBundle.changeStatusToSubmitted(moneyRequestId, moneyRequestBundleId, moneyRequestBundlesRepository), actorSystem.dispatcher());
		};
	}*/
/*
	private static MoneyRequestBundle changeStatusToSubmitted(String moneyRequestId, String moneyRequestBundleId, MoneyRequestBundlesRepository moneyRequestBundlesRepository) {
		MoneyRequestBundle moneyRequestBundle = findMoneyRequestBundle(moneyRequestBundleId, moneyRequestBundlesRepository);
		MoneyRequest moneyRequest = moneyRequestBundle.findMoneyRequestById(moneyRequestId);

		moneyRequest.changeStatus("submitted");

		return moneyRequestBundlesRepository.update(moneyRequestBundleId, moneyRequestBundle);
	}
*/
	public MoneyRequest findMoneyRequestById(String moneyRequestId) {
		return requests.stream().filter(moneyRequest -> moneyRequest.getId().equals(moneyRequestId)).findFirst().get();
	}

	/*
	private static MoneyRequestBundle findMoneyRequestBundle(String id, MoneyRequestBundlesRepository repository) {
		try {
			return repository.findById(id);
		} catch (EntityNotFound entityNotFound) {
			throw new IllegalArgumentException();
		}
	}*/

	public Collection<MoneyRequest> getRequests() {
		return ImmutableList.copyOf(requests);
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

	public String getType() {
		return this.type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public Date getCreationDate() {
		return creationDate;
	}

	public void setViolations(Collection<Violation> violations) {
		this.violations = violations;
	}
	public void setMoneyRequestBundlesRepository(MoneyRequestBundlesRepository moneyRequestBundlesRepository) {
		this.moneyRequestBundlesRepository = moneyRequestBundlesRepository;
	}

	public String getBucksId() {
		return bucksId;
	}

	public boolean hasViolations() {
		return !violations.isEmpty();
	}

	public Collection<Violation> getViolations() {
		return violations;
	}

	public String getSourceCommand() {
		return sourceCommand;
	}
	
	public void setSourceCommand(String sourceCommand) {
		this.sourceCommand = sourceCommand;
	}

	void setTimeServer(TimeServer timeServer) {
		this.timeServer = timeServer;
	}

	private Collection<MoneyRequest> requests;

	private Collection<Violation> violations = ImmutableSet.of();

	private String reason;

	private String bucksId;

	private String id;

	private String type = "moneyRequest";

	private String status = "ready";

	private Date creationDate;

	private String sourceCommand;
	


	@Autowired
	private MoneyRequestBundlesRepository moneyRequestBundlesRepository;


	@Autowired
	private TimeServer timeServer;


}
