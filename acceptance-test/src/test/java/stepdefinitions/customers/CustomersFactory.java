package stepdefinitions.customers;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;
import stepdefinitions.DefaultFactory;

import java.util.*;

@Component
public class CustomersFactory extends DefaultFactory {

	public Map<String, Object> createCustomer(String identityCard) {
		Map<String, Object> customer = new HashMap<>();

		customer.put("id", UUID.randomUUID().toString());
		customer.put("name", "Alfred");
		customer.put("lastName", "Hitchcock");
		customer.put("email", "Hitchcock@gmail.com");
		customer.put("idCard", identityCard);
		customer.put("mobilePhone", UUID.randomUUID().toString());
		customer.put("address", createAddress());
		customer.put("contactDetails", createContacts());
		customer.put("termsAndConditions", true);
		customer.put("bankingServiceAgreementId", UUID.randomUUID().toString());
		customer.put("bankingServiceCardId", UUID.randomUUID().toString());

		createSettings(customer);

		return customer;
	}

	public Map<String, List<Object>> getSkeleton(Map<String, Object> customer) {

		Map<String, Object> address = new HashMap<>();
		Map<String, Object> geoLocation = new HashMap<>();
		Map<String, List<Object>> skeleton = new HashMap<>();
		skeleton.put("name", ImmutableList.of(customer, "Bob"));
		skeleton.put("lastName", ImmutableList.of(customer, "Dylan"));
		skeleton.put("mobilePhone", ImmutableList.of(customer, "777777777"));
		skeleton.put("email", ImmutableList.of(customer, "bob@dylan.com"));
		skeleton.put("idCard", ImmutableList.of(customer, "50861048K"));
		skeleton.put("termsAndConditions", ImmutableList.of(customer, true));
		skeleton.put("address", ImmutableList.of(customer, address));
		skeleton.put("addressType", ImmutableList.of(address, "Rue"));
		skeleton.put("streetAddress", ImmutableList.of(address, "Ordener"));
		skeleton.put("streetNumber", ImmutableList.of(address, "1"));
		skeleton.put("postcode", ImmutableList.of(address, "75018"));
		skeleton.put("town", ImmutableList.of(address, "Paris"));
		skeleton.put("country", ImmutableList.of(address, "France"));
		skeleton.put("geolocation", ImmutableList.of(address, geoLocation));
		skeleton.put("longitude", ImmutableList.of(geoLocation, 48.89364));
		skeleton.put("latitude", ImmutableList.of(geoLocation, 2.33739));

		return skeleton;
	}

	private void createSettings(final Map<String, Object> customerData) {
		HashMap<Object, Object> settings = new HashMap<>();
		customerData.put("setting", settings);

		HashMap<Object, Object> notificationValues = new HashMap<>();

		HashMap<Object, Object> notifications = new HashMap<>();
		notifications.put("notifications", notificationValues);

		notificationValues.put("tooltips", true);
		notificationValues.put("alerts", true);
		notificationValues.put("news", true);

		customerData.put("settings", notifications);
	}

	private Map<String, Object> createAddress() {
		Map<String, Object> adress = new HashMap<>();

		adress.put("addressType", "calle");
		adress.put("streetAddress", "Maldonado");
		adress.put("streetNumber", 1);
		adress.put("houseNumber", 3);
		adress.put("houseLetter", "A");
		adress.put("postcode", "28006");
		adress.put("town", "Madrid");

		return adress;
	}

	private Collection<Map<String, Object>> createContacts() {
		return new ArrayList<Map<String, Object>>() {

			private static final long serialVersionUID = 1L;

			{
				add(createEmailContact());
				add(createPhoneContact());
			}
		};
	}

	private Map<String, Object> createEmailContact() {
		Map<String, Object> emailContact = new HashMap<>();

		emailContact.put("contact", "alfred.hitchcock" + Math.abs(new Random().nextInt()) + "@indiscreet.com");
		emailContact.put("contactType", "personal_email");

		return emailContact;
	}

	private Map<String, Object> createPhoneContact() {
		Map<String, Object> phoneContact = new HashMap<>();

		phoneContact.put("contact", "+34" + Math.abs(new Random().nextInt()));
		phoneContact.put("contactType", "personal_phone");

		return phoneContact;
	}
}

