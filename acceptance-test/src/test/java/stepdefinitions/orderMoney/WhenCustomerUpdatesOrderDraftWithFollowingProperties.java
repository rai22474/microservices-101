package stepdefinitions.orderMoney;

import com.google.common.collect.ImmutableMap;
import cucumber.api.java.en.When;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.customers.CustomersRegistry;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;


@ContextConfiguration("classpath:cucumber.xml")
public class WhenCustomerUpdatesOrderDraftWithFollowingProperties {
    @When("^customer \"([^\"]*)\" updates the money order draft with the following properties$")
    public void customer_updates_the_money_order_draft_with_the_following_properties(String senderIdCard, List<Map<String, Object>> ordersData) throws Throwable {

        Response moneyOrderDraftResponse = restClient.put(
                "drafts/moneyOrders/" + cucumberContext.getValue("moneyOrderDraftId"),
                moneyOrderFactory.createDefaultBundle(ordersData),
                ImmutableMap.of("x-customer-id", customersRegistry.getCustomerId(senderIdCard)));

        cucumberContext.publishValue("response", moneyOrderDraftResponse);
        cucumberContext.publishValue("responseEntity", restJsonReader.read(moneyOrderDraftResponse));
    }


    @Autowired
    private RestClient restClient;

    @Autowired
    private CucumberContext cucumberContext;

    @Autowired
    private MoneyOrdersFactory moneyOrderFactory;

    @Autowired
    private RestJsonReader restJsonReader;

    @Autowired
    private CustomersRegistry customersRegistry;
}
