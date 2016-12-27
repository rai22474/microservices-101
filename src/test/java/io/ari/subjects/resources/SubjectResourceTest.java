package io.ari.subjects.resources;

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
public class SubjectResourceTest {

	private static final String SUBJECT_ID = "ES0182000003082";

	@InjectMocks
	private SubjectResource subjectsResource;
	
	@Mock
	private EntityRepository subjectsRepository;
	
	@Test
	public void shouldDeleteAnExistingUserResponse(){
		ResponseEntity response = subjectsResource.deleteClient(SUBJECT_ID);
		assertEquals("The response code should be NO CONTENT", 204, response.getStatusCodeValue());
		
		verify(subjectsRepository).delete(SUBJECT_ID);
	}
}