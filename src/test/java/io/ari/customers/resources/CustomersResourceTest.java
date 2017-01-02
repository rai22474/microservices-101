package io.ari.customers.resources;

import com.google.common.collect.ImmutableMap;
import io.ari.customers.domain.exceptions.CustomerExists;
import io.ari.customers.domain.repositories.CustomersRepository;
import io.ari.customers.resources.assemblers.CustomersAssembler;
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
				createCustomerData());

		assertEquals("The response code should be CREATED", 201, response.getStatusCodeValue());
		verify(customersRepository).saveEntity(CUSTOMER_ID, createCustomerData());
	}
	
	@Test
	public void shouldReturnTheEntityResponse() throws CustomerExists {
		Map<String, Object> customerData = createCustomerData();
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
	
	private Map<String, Object> createCustomerData() {
		Map<String, Object> customer = new HashMap<>();

		customer.put("Nationality", "IT");
		customer.put("Gender", "V");
		customer.put("Name", "Mr");
		customer.put("Surname1", "Smith");
		customer.put("Surname2", "Smith");
		customer.put("Birthdate", "IT");
		customer.put("LegalId", "38087025");
		customer.put("email", "38087025@smith-smith.com");
		customer.put("entity", "0182");
		customer.put("totalCode", "ES0182000003082");
		customer.put("CivilStatus", "");
		customer.put("LegalForm", "");

		return customer;
	}

	@Mock
	private CustomersAssembler customersAssembler;

	@Mock
	private CustomersRepository customersRepository;

	@InjectMocks
	private CustomersResource customersResource;

	private static String CUSTOMER_ID = "12345678";
}
