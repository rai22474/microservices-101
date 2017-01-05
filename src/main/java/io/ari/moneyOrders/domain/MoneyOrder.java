
package io.ari.moneyOrders.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.repositories.entities.Entity;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public class MoneyOrder implements Entity{

	@JsonCreator
	public MoneyOrder(@JsonProperty("id") String id,
					  @JsonProperty("amount") Money amount,
					  @JsonProperty("bucksId") String bucksId,
					  @JsonProperty("recipient") Recipient recipient,
					  @JsonProperty("status") String status) {
		this.id = id;
		this.amount = amount;
		this.bucksId = bucksId;
		this.recipient = recipient;
		this.status = status;
	}

	public MoneyOrder(String id) {
		this.id = id;
	}

	public void changeStatus(String newStatus) {
		setStatus(newStatus);
	}

	public Money getAmount() {
		return amount;
	}

	public void setAmount(Money amount) {
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

	public Recipient getRecipient() {
		return recipient;
	}

	public void setRecipient(Recipient recipient) {
		this.recipient = recipient;
	}

	public String submit() {
		return recipient.requestMoneyOrder(getBucks(), amount, id);
	}

	public void processTransferEvent(Map<String, Object> event) {
		recipient.confirmMoneyOrder(getBucks(), event);
	}



	public boolean hasViolations() {
		return !violations.isEmpty();
	}

	public Collection<Violation> getViolations() {
		return violations;
	}

	void setBucksRepository(BucksRepository bucksRepository) {
		this.bucksRepository = bucksRepository;
	}

	public String getBucksId() {
		return bucksId;
	}

	public void setBucksId(String bucksId) {
		this.bucksId = bucksId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private Bucks getBucks() {
		if (bucks == null) {
			try {
				bucks = bucksRepository.findById(bucksId);
			} catch (EntityNotFound e) {
				throw new IllegalStateException("The money order bucks don't exist.", e);
			}
		}

		return bucks;
	}

	private Collection<Violation> violations = ImmutableSet.of();


	private final String id;

	private Money amount = new Money(BigDecimal.ZERO, "EUR");

	private Recipient recipient;

	private String bucksId;

	@Autowired
	private BucksRepository bucksRepository;

	private Bucks bucks;

	private String status = "new";
}
