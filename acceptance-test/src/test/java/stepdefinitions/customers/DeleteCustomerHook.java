package stepdefinitions.customers;

import cucumber.api.java.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:cucumber.xml")
public class DeleteCustomerHook {

    @After(value = "@deleteCustomer", order = 10)
    public void deleteCustomers() {
        customerRegistry.deleteCustomers();
    }

    @Autowired
    private CustomersRegistry customerRegistry;

}
