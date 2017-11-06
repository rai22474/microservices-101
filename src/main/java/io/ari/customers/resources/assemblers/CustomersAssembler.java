package io.ari.customers.resources.assemblers;

import com.google.common.collect.ImmutableList;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.customers.domain.Customer;
import io.ari.customers.domain.exceptions.CustomerExists;
import io.ari.customers.domain.factories.CustomersFactory;
import io.ari.customers.resources.assemblers.exceptions.InvalidIdCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Maps.newHashMap;

@Component
public class CustomersAssembler{

    public Customer convertDtoToEntity(String customerId, Map<String, Object> customerData) throws CustomerExists, InvalidIdCard {
        String idCard = (String) customerData.get("idCard");
        if (!idCardValidator.isValid(idCard)){
            throw new InvalidIdCard();
        }

        Customer customer = customersFactory.createCustomer(customerId,
                idCard,
                (String) customerData.get("name"),
                (String) customerData.get("lastName"),
                (String) customerData.get("mobilePhone"));

        customer.setEmail((String) customerData.get("email"));
        customer.setAvatar((String) customerData.get("avatar"));

        return customer;
    }

    public Map<String, Object> convertEntityToDto() {
        return of("_links", hypermediaAssembler.createHypermedia("api/me", "ari-read"));
    }

    public Map<String, Object> convertEntityToDto(Customer customer) {
        Map<String, Object> customerDto = new HashMap<>();
        customerDto.put("name",customer.getName());
        customerDto.put("settings",customer.getSettings());
        customerDto.put("lastName", customer.getLastName());
        customerDto.put("idCard", customer.getIdCard());
        customerDto.put("email", customer.getEmail());
        customerDto.put("address", customer.getAddress());

        customerDto.put("contactDetails", ImmutableList.of(createEmailContact(customer.getEmail()),
                createPhoneContact(customer.getMobilePhone())));
        customerDto.put("_links", getCustomerHypermedia());

        return customerDto;
    }

    private Map<String, Object> createEmailContact(String email) {
        Map<String, Object> emailContact = newHashMap();

        emailContact.put("contact", email);
        emailContact.put("contactType", "personal_email");

        return emailContact;
    }

    private Map<String, Object> createPhoneContact(String phone) {
        Map<String, Object> phoneContact = newHashMap();

        phoneContact.put("contact", phone);
        phoneContact.put("contactType", "personal_phone");

        return phoneContact;
    }

    protected String getCollectionSelfLink() {
        return "api/customers";
    }

    private Map<String, Object> getCustomerHypermedia() {
        Map<String, Object> hypermedia = hypermediaAssembler.createHypermedia("api/me", "ari-read");

        hypermedia.put("bucks", hypermediaAssembler.createLink("api/bucks", "GET", "ari-read"));
        hypermedia.put("movements", hypermediaAssembler.createLink("api/movements", "GET", "ari-read"));
        hypermedia.put("settings", hypermediaAssembler.createLink("api/settings", "GET", "ari-read"));
        hypermedia.put("editSettings", hypermediaAssembler.createLink("api/settings", "PUT", "ari-write"));
        hypermedia.put("editMe", hypermediaAssembler.createLink("api/me", "PUT", "ari-write"));

        hypermedia.put("createMoneyOrder", hypermediaAssembler.createLink("api/moneyOrders", "POST", "ari-write"));
        hypermedia.put("createMoneyOrderDraft", hypermediaAssembler.createLink("api/drafts/moneyOrders", "POST", "ari-write"));

        hypermedia.put("createMoneyRequest", hypermediaAssembler.createLink("api/moneyRequests", "POST", "ari-write"));
        hypermedia.put("createMoneyRequestDraft", hypermediaAssembler.createLink("api/drafts/moneyRequests", "POST", "ari-write"));

        hypermedia.put("recharge", hypermediaAssembler.createLink("api/recharges", "POST", "ari-recharges"));
        hypermedia.put("rechargeCards", hypermediaAssembler.createLink("api/cards", "GET", "ari-recharges"));

        hypermedia.put("wallet", hypermediaAssembler.createLink("api/cards", "GET", "ari-read"));
        hypermedia.put("createCard", hypermediaAssembler.createLink("api/cards", "POST", "ari-write"));

        return hypermedia;
    }

    @Autowired
    private CustomersFactory customersFactory;

    @Autowired
    private HypermediaAssembler hypermediaAssembler;

    @Autowired
    private IdCardValidator idCardValidator;

}
