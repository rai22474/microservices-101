package io.ari.moneyOrders.domain.recipients;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ari.bucks.domain.Bucks;
import io.ari.money.domain.Money;
import io.ari.time.TimeServer;
import io.ari.uidGenerator.UIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.HashMap;
import java.util.Map;

@Configurable(dependencyCheck = true)
public class BucksRecipient extends Recipient {

	public BucksRecipient(String targetBucksId) {
		super(new HashMap<>());

		this.targetBucksId = targetBucksId;
		getData().put("targetBucksId", targetBucksId);
	}

	@JsonCreator
	public BucksRecipient(@JsonProperty("targetBucksId") String targetBucksId,
						  @JsonProperty("data") Map<String, Object> data) {
		super(data);
		this.targetBucksId = targetBucksId;
	}

	@Override
	public String requestMoneyOrder(Bucks sourceBucks, Money amount, String operationId) {
		/*return bankingService.transfer(
				sourceBucks.getBankingServiceAgreementId(),
				getTargetBucks().getBankingServiceAgreementId(),
				amount,
				operationId);*/
		return "";
	}

	@Override
	public void confirmMoneyOrder(Bucks sourceBucks, Map<String, Object> event) {
		Money amount = (Money) event.get("amount");

		//Bucks targetBucks = getTargetBucks();

		sourceBucks.withdrawMoney(amount);
		//targetBucks.receiveMoney(amount);

		//bucksRepository.update(sourceBucks.getId(), sourceBucks);
		//bucksRepository.update(targetBucks.getId(), targetBucks);
	}

	@Override
	public String submitMoneyRequest(String sourceBucksId, Money amount, String reason, String sourceCommand) {
	/*	MoneyRequestReception moneyRequestReception = new MoneyRequestReception(targetBucksId,
				sourceBucksId,
				amount,
				reason,
				sourceCommand, 
				timeServer.currentDate(),uidGenerator);

		MoneyRequestReception savedMoneyRequestReception = moneyRequestReceptionRepository.save(moneyRequestReception);

		eventsPublisher.publish(
				factory.createFact(amount,
						sourceBucksId,
						targetBucksId,
						"moneyRequest",
						reason,
						savedMoneyRequestReception.getId()));*/

		return "";
	}

	@Override
	public Recipient clone() {
		return new BucksRecipient(targetBucksId, getData());
	}

	public String getTargetBucksId() {
		return targetBucksId;
	}

	@Override
	public String getType() {
		return "bucksRecipient";
	}

	@Override
	public boolean isTheSameAs(String bucksId) {
		return targetBucksId.equals(bucksId);
	}

	private String targetBucksId;

	//@Autowired
	//private FactsFactory factory;

	//@Autowired
	//private EventsPublisher eventsPublisher;

	//@Autowired
	//private EntitiesRepository<MoneyRequestReception> moneyRequestReceptionRepository;
	
	@Autowired
	private TimeServer timeServer;

	@Autowired
	private UIDGenerator uidGenerator;
}
