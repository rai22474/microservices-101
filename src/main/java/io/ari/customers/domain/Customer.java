package io.ari.customers.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableMap;
import io.ari.repositories.entities.Entity;

import java.util.Map;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;


@JsonSerialize(include = NON_NULL)
public class Customer implements Entity {

	@JsonCreator
	public Customer(@JsonProperty("id") String id,
					@JsonProperty("idCard") String idCard) {
		this.id = id;
		this.idCard = idCard;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getIdCard() {
		return idCard;
	}

	public Map<String, Object> getAddress() {
		return address;
	}

	public void setAddress(Map<String, Object> address) {
		this.address = address;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Map<String, Object> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, Object> settings) {
		this.settings = settings;
	}

	private String id;

	private String name;

	private String lastName;

	private String email;

	private String mobilePhone;

	private String idCard;

	private Map<String, Object> address = ImmutableMap.of();

	private String avatar;

	@JsonProperty("settings")
	private Map<String, Object> settings;

}
