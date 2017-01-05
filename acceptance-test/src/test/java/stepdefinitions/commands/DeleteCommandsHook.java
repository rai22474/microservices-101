package stepdefinitions.commands;

import cucumber.api.java.After;
import io.ari.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:cucumber.xml")
public class DeleteCommandsHook {

	@After("@deleteCommands")
	public void deleteCommands() {
		dataLoaderClient.delete("commands");
	}

	@Autowired
	@Qualifier("dataLoaderClient")
	private RestClient dataLoaderClient;

}
