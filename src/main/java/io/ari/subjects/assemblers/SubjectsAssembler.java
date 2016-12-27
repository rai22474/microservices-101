package io.ari.subjects.assemblers;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SubjectsAssembler {

	public Map<String, Object> createEntityFromDto(Map<String, Object> subjectDataDto) {
		return new HashMap<String,Object>(subjectDataDto);
	}
	
}
