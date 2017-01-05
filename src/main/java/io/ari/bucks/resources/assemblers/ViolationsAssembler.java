package io.ari.bucks.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.bussinessRules.Violation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViolationsAssembler {
	
	public Collection<Map<String, Object>> convertEntitiesToDtos(Collection<Violation> violations) {
		return violations.stream()
					.map(this::convertEntityToDto)
					.collect(Collectors.toList());
	}
	
	public Map<String,Object> convertEntityToDto(Violation violation){
		return ImmutableMap.of("code",violation.getCode(),
				"description",violation.getMessage());
	}
}
