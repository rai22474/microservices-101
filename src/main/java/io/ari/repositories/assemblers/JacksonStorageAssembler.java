package io.ari.repositories.assemblers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class JacksonStorageAssembler<T> extends StorageAssembler<T>{

	public JacksonStorageAssembler(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public Map<String, Object> convertEntityToDto(T entity) {
		return objectMapper.convertValue(entity, Map.class);
	}

	public T convertDtoToEntity(Map<String, Object> dto) {
		return objectMapper.convertValue(dto, entityClass);
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Autowired
	private ObjectMapper objectMapper;

	private Class<T> entityClass;
}
