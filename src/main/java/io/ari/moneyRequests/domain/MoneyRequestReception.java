package io.ari.moneyRequests.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.moneyOrders.domain.recipients.BucksRecipient;
import io.ari.repositories.entities.Entity;
import io.ari.uidGenerator.UIDGenerator;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.Date;
import java.util.UUID;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include = NON_NULL)
@Configurable(dependencyCheck = true)
public class MoneyRequestReception implements Entity {

	@JsonCreator
	public MoneyRequestReception(@JsonProperty("id") String id,
								 @JsonProperty("sourceCommand") String sourceCommand,
								 @JsonProperty("status") String status,
								 @JsonProperty("moneyOrderBundle") MoneyOrderBundle moneyOrderBundle) {
		this.id = id;
		this.status = status;
		this.sourceCommand = sourceCommand;
		this.moneyOrderBundle = moneyOrderBundle;
	}

	public MoneyRequestReception(String sourceBucksId, 
			String targetBucksId, 
			Money amount,
			String reason, 
			String sourceCommand, Date currentDate,UIDGenerator uidGenerator) {
		MoneyOrder moneyOrder = new MoneyOrder(uidGenerator.generateUID());
		
		moneyOrder.setAmount(amount);
		moneyOrder.setRecipient(new BucksRecipient(targetBucksId));
		moneyOrder.setBucksId(sourceBucksId);

		moneyOrderBundle = new MoneyOrderBundle(UUID.randomUUID().toString(), currentDate, sourceBucksId, moneyOrder);
		moneyOrderBundle.setReason(reason);

		this.sourceCommand = sourceCommand;
	}

	public void reject() {
		status = "rejected";
		//publishStatusChangedFact("moneyRequestRejection");
	}

	public void accept(String reason) {
		status = "accepted";
		//publishStatusChangedFact("moneyRequestAcceptance");
	}

	public MoneyOrderBundle getMoneyOrderBundle() {
		return moneyOrderBundle;
	}

	public String getStatus() {
		return status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceCommand() {
		return sourceCommand;
	}

	/*
	private void publishStatusChangedFact(String factType) {
		eventsPublisher.publish(factory.createFact(factType, id));
	}
*/
	private MoneyOrderBundle moneyOrderBundle;

	private String status = "new";

	private String sourceCommand;

	private String id;

	/*
	@Autowired
	private FactsFactory factory;

	@Autowired
	private EventsPublisher eventsPublisher;*/
}
