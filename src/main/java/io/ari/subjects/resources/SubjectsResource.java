package io.ari.subjects.resources;

import io.ari.repositories.write.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("io/ari/subjects")
public class SubjectsResource {

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity createSubject(@RequestBody Map<String, Object> subjectData) {
		System.out.println(subjectData);
		return createClient(subjectData);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity deleteSubjects() {
		subjectsRepository.deleteAll();
		return ResponseEntity.noContent().build();
	}

	private ResponseEntity<Map<String,Object>> createClient(Map<String, Object> subjectData) {
		Map<String,Object> savedEntity = subjectsRepository.saveEntity(subjectData);

		Map<String, Object> entity = new HashMap<>();
		entity.put(ENTITY_ID, savedEntity.get("id"));

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(entity);
	}

	private static final String ENTITY_ID = "entityId";

	@Autowired
	@Qualifier("subjectsRepository")
	private EntityRepository subjectsRepository;
}
