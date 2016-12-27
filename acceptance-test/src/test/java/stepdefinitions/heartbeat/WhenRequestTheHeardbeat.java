package stepdefinitions.heartbeat;

import cucumber.api.java.en.When;
import io.ari.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;

@ContextConfiguration("classpath:cucumber.xml")
public class WhenRequestTheHeardbeat {

    @When("^i request a heart beat$")
    public void i_request_a_heart_beat() {
        Response response = requestSender.get("heartbeat");
        cucumberContext.publishValue("response", response);
    }

    @Autowired
    @Qualifier("readClient")
    private RestClient requestSender;

    @Autowired
    private CucumberContext cucumberContext;

}
