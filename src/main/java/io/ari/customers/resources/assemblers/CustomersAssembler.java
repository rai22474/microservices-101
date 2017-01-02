package io.ari.customers.resources.assemblers;

import com.google.common.collect.ImmutableSet;
import io.ari.assemblers.Assembler;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.customers.domain.Customer;
import io.ari.customers.domain.exceptions.CustomerExists;
import io.ari.customers.domain.factories.CustomersFactory;

import io.ari.customers.resources.assemblers.exceptions.InvalidIdCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.stream.Collectors.toMap;

@Component
public class CustomersAssembler extends Assembler {

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

    @Override
    public Map<String, Object> convertEntityToDto(Map<String, Object> customerData, Object... additionalData) {
        Map<String, Object> customerDto = customerData
                .entrySet()
                .stream()
                .collect(toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        customerDto.put("_links", getCustomerHypermedia());

        return customerDto;
    }

    @Override
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
