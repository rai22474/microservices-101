package stepdefinitions.customers;

import com.google.common.collect.ImmutableMap;
import io.ari.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;

@Component
@Scope("cucumber-glue")
public class CustomersRegistry {

    public void registerCustomer(String id, String code) {
        customersByCode.put(code, id);
        customersId.add(id);
    }

    public void deleteCustomers() {
        customersId
                .stream()
                .forEach(this::deleteCustomer);
    }

    public String getCustomerId(String customerIdCard) {
        return customersByCode.get(customerIdCard);
    }

    private void deleteCustomer(String customerIdentifier) {
        Response response = restClient.delete("me", ImmutableMap.of("x-customer-id", customerIdentifier));
        assertEquals("The customer:" + customerIdentifier + " -> must be correctly deleted", 204, response.getStatus());
    }

    @Autowired
    private RestClient restClient;

    private Map<String, String> customersByCode = newHashMap();

    private List<String> customersId = new ArrayList<>();
}
