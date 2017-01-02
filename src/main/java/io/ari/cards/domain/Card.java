package io.ari.cards.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;
import io.ari.repositories.entities.Entity;

import java.util.List;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include = NON_NULL)
public class Card implements Entity {

	public Card(String customerId, String customerName, String customerLastname, String bankingServiceAgreementId, String type, String bankingServiceCardId, String pan, String image, String status) {
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerLastname = customerLastname;
		this.type = type;
		this.bankingServiceAgreementId = bankingServiceAgreementId;
		this.bankingServiceCardId = bankingServiceCardId;
		this.pan = pan;
		this.image = image;
		this.status = status;
	}

	@JsonCreator
	public Card(
			@JsonProperty("id") String id,
			@JsonProperty("participants.id") String customerId,
			@JsonProperty("name") String customerName,
			@JsonProperty("lastName") String customerLastname,
			@JsonProperty("bankingServiceAgreementId") String bankingServiceAgreementId,
			@JsonProperty("cardType") String type,
			@JsonProperty("bankingServiceCardId") String bankingServiceCardId,
			@JsonProperty("pan") String pan,
			@JsonProperty("image") String image,
			@JsonProperty("status") String status) {

		this(customerId, customerName, customerLastname, bankingServiceAgreementId, type, bankingServiceCardId, pan, image, status);
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public String getBankingServiceAgreementId() {
		return bankingServiceAgreementId;
	}

	public String getBankingServiceCardId() {
		return bankingServiceCardId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getPan() {
		return pan;
	}

	public String getImage() {
		return image;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getCustomerLastname() {
		return customerLastname;
	}

    @JsonIgnore
    public boolean isTva() {
        return Type.TVA.equals(type);
    }

	public String getStatus() {
		return status;
	}

	@JsonIgnore
	public boolean isBlocked() {
		return BLOCKED_STATUSES.contains(status);
	}

	public void block() {
        status = Status.BLOCKED;
	}

	@JsonIgnore
	public boolean isActive() {
		return Status.ACTIVE.equals(status);
	}

	@JsonIgnore
	public boolean isInactive() {
		return Status.INACTIVE.equals(status);
	}

	void setStatus(String status) {
		this.status = status;
	}

	private String id;

	@JsonProperty("cardType")
	private String type;

	private String bankingServiceAgreementId;

	private String bankingServiceCardId;

	private String customerId;

	@JsonProperty("name")
	private String customerName;

	@JsonProperty("lastName")
	private String customerLastname;

	private String pan;

	private String image;

	private String status;

    private static final List<String> BLOCKED_STATUSES = ImmutableList.of(Status.BLOCKED);

    public static class Status {
        public static final String BLOCKED = "blocked";
        public static final String ACTIVE = "active";
        public static final String INACTIVE = "inactive";
    }

    private static class Type {
        public static final String TVA = "tva";
    }
}
