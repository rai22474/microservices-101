package io.ari.customers.resources;

import io.ari.repositories.write.EntityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CustomerAdminResourceTest {

	private static final String CUSTOMER_ID = "ES0182000003082";

	@InjectMocks
	private CustomerAdminResource customersResource;
	
	@Mock
	private EntityRepository customersRepository;
	
	@Test
	public void shouldDeleteAnExistingUserResponse(){
		ResponseEntity response = customersResource.deleteClient(CUSTOMER_ID);
		assertEquals("The response code should be NO CONTENT", 204, response.getStatusCodeValue());
		
		verify(customersRepository).delete(CUSTOMER_ID);
	}
}