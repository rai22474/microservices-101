package io.ari.customers.resources.assemblers;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomersAdminAssembler {

	public Map<String, Object> createEntityFromDto(Map<String, Object> subjectDataDto) {
		return new HashMap<String,Object>(subjectDataDto);
	}
	
}
