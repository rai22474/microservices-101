package stepdefinitions.customers;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CustomersFactory {

	public Map<String, Object> createCustomer(String identityCard) {
		Map<String, Object> customer = new HashMap<>();

		customer.put("name", "Alfred");
		customer.put("lastName", "Hitchcock");
		customer.put("idCard", identityCard);

		customer.put("address", createAdress());
		customer.put("contactDetails", createContacts());

		createSettings(customer);

		return customer;
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

	private Map<String, Object> createAdress() {
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

