package io.ari.customers.resources;

import com.google.common.collect.ImmutableMap;
import io.ari.customers.domain.exceptions.CustomerExists;
import io.ari.repositories.entities.Repository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class CustomersResourceTest {

	@Test
	public void shouldReturnTheCreatedResponse() throws CustomerExists {
		ResponseEntity response = customersResource.createCustomer(CUSTOMER_ID,
				createClientData());

		assertEquals("The response code should be CREATED", 201, response.getStatusCodeValue());
		verify(customersRepository).saveEntity(CUSTOMER_ID,createClientData());
	}
	
	@Test
	public void shouldReturnTheEntityResponse() throws CustomerExists {
		Map<String, Object> customerData = createClientData();
		when(customersRepository.saveEntity(CUSTOMER_ID,customerData)).thenReturn(ImmutableMap.of("id",CUSTOMER_ID));

		ResponseEntity response = customersResource.createCustomer(CUSTOMER_ID,customerData);

		Map<String,Object> entity = (Map<String,Object>)response.getBody();

		assertNotNull("The response entity must not null",entity);
		assertEquals("The id must be expected", CUSTOMER_ID, entity.get("entityId"));
		verify(customersRepository).saveEntity(CUSTOMER_ID,customerData);
	}
	
	@Test
	public void shouldDeleteCustomerCollection(){
		ResponseEntity response = customersResource.deleteCustomers();
		assertEquals("The response code should be NO CONTENT", 204, response.getStatusCodeValue());
		verify(customersRepository).deleteAll();
	}
	
	private Map<String, Object> createClientData() {
		Map<String, Object> client = new HashMap<>();

		client.put("Nationality", "IT");
		client.put("Gender", "V");
		client.put("Name", "Mr");
		client.put("Surname1", "Smith");
		client.put("Surname2", "Smith");
		client.put("Birthdate", "IT");
		client.put("LegalId", "38087025");
		client.put("email", "38087025@smith-smith.com");
		client.put("entity", "0182");
		client.put("totalCode", "ES0182000003082");
		client.put("CivilStatus", "");
		client.put("LegalForm", "");

		return client;
	}

	@Mock
	private Repository customersRepository;

	@InjectMocks
	private CustomersResource customersResource;

	private static String CUSTOMER_ID = "12345678";
}
