
package io.ari.moneyRequests.domain;

import com.google.common.collect.ImmutableSet;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.repositories.entities.Entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

public class MoneyRequest implements Entity {

	public MoneyRequest(String id) {
		this.id = id;
	}
	
	public MoneyRequest(String id, Money amount, Recipient recipient) {
		this.id = id;
		this.amount = amount;
		this.recipient = recipient;
	}

	public MoneyRequest clone(){
		MoneyRequest cloned = new MoneyRequest(UUID.randomUUID().toString(),
				getAmount(),
				getRecipient().clone());
		cloned.setBucksId(bucksId);
		return cloned;
	}

	public boolean hasViolations() {
		return !violations.isEmpty();
	}

	public Collection<Violation> getViolations() {
		return violations;
	}

	public String submit(String sourceBucks, String reason) {
		return recipient.submitMoneyRequest(sourceBucks, amount, reason, id);
	}

	public String getBucksId() {
		return bucksId;
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

	public void changeStatus(String newStatus) {
		setStatus(newStatus);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setBucksId(String bucksId) {
		this.bucksId = bucksId;
	}

	private final String id;

	private Money amount = new Money(BigDecimal.ZERO, "EUR");

	private Recipient recipient;

	private String status = "new";

	private String bucksId;

	private Collection<Violation> violations = ImmutableSet.of();
}
