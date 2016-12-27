package io.ari.subjects.resources;

import com.google.common.collect.ImmutableMap;
import io.ari.repositories.write.EntityRepository;
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

@RunWith(MockitoJUnitRunner.class)
public class SubjectsResourceTest {

	@Test
	public void shouldReturnTheCreatedResponse() {
		ResponseEntity response = subjectsResource.createSubject(createClientData());

		assertEquals("The response code should be CREATED", 201, response.getStatusCodeValue());
		verify(subjectsRepository).saveEntity(createClientData());
	}
	
	@Test
	public void shouldReturnTheEntityResponse() {
		Map<String, Object> subjectData = createClientData();
		when(subjectsRepository.saveEntity(subjectData)).thenReturn(ImmutableMap.of("id",SUBJECT_ID));

		ResponseEntity response = subjectsResource.createSubject(subjectData);

		Map<String,Object> entity = (Map<String,Object>)response.getBody();

		assertNotNull("The response entity must not null",entity);
		assertEquals("The id must be expected", SUBJECT_ID, entity.get("entityId"));
		verify(subjectsRepository).saveEntity(subjectData);
	}
	
	@Test
	public void shouldDeleteSubjectCollection(){
		ResponseEntity response = subjectsResource.deleteSubjects();
		assertEquals("The response code should be NO CONTENT", 204, response.getStatusCodeValue());
		verify(subjectsRepository).deleteAll();
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
	private EntityRepository subjectsRepository;

	@InjectMocks
	private SubjectsResource subjectsResource;

	private static String SUBJECT_ID = "12345678";
}
