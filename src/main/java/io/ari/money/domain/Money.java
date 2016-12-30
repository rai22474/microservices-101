package io.ari.money.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Money {

	@JsonCreator
	public Money(
			@JsonProperty("value") Number amount,
			@JsonProperty("currency") String isoCode) {
		this.amount = new BigDecimal(amount.toString());
		this.currencyCode = isoCode;
	}

	public boolean isEnoughFor(Money money) {
		if (!currencyCode.equals(money.currencyCode)) {
			throw new IllegalStateException("Invalid currency");
		}

		return this.amount.compareTo(money.amount) != -1;
	}

	public Money add(Money money) {
		if (!currencyCode.equals(money.currencyCode)) {
			throw new IllegalStateException("Invalid currency");
		}

		return new Money(money.amount.add(this.amount), money.currencyCode);
	}

	public Money subtract(Money money) {
		if (!currencyCode.equals(money.currencyCode)) {
			throw new IllegalStateException("Invalid currency");
		}

		return new Money(this.amount.subtract(money.amount), money.currencyCode);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public Money negate() {
		return new Money(this.amount.negate(), this.currencyCode);
	}

	@Override
	public String toString() {
		return "Money [amount=" + amount + ", currencyCode=" + currencyCode + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Money other = (Money) obj;
		if (amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!amount.equals(other.amount)) {
			return false;
		}
		if (currencyCode == null) {
			if (other.currencyCode != null) {
				return false;
			}
		} else if (!currencyCode.equals(other.currencyCode)) {
			return false;
		}
		return true;
	}

	@JsonProperty("value")
	final private BigDecimal amount;

	@JsonProperty("currency")
	final private String currencyCode;


}
