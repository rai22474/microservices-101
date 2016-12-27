package io.ari.subjects.resources;

import io.ari.repositories.write.EntityRepository;
import io.ari.subjects.assemblers.SubjectsAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("io/ari/subjects/{subjectId}")
public class SubjectResource {

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity deleteClient(@PathVariable("subjectId") String subjectId) {
		subjectsRepository.delete(subjectId);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity updateClient(@PathVariable("subjectId")  String subjectId, @RequestBody Map<String, Object> subjectData) {
		Map<String, Object> entity = subjectsAssembler.createEntityFromDto(subjectData);
		subjectsRepository.update(subjectId,entity);
		return ResponseEntity.ok().build();
	}
	
	@Autowired
	@Qualifier("subjectsRepository")
	private EntityRepository subjectsRepository;
	
	@Autowired
	private SubjectsAssembler subjectsAssembler;
}
