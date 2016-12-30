package io.ari.repositories.assemblers;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Parent class for all the class that serialize domain entities in a map. 
 */
public abstract class StorageAssembler<T> {

	public abstract Map<String, Object> convertEntityToDto(T entity);
	
	public abstract T convertDtoToEntity(Map<String, Object> dto);

	public Collection<T> convertDtosToDomainEntities(Collection<Map<String, Object>> dtos) {
		return dtos.stream()
				.map(this::convertDtoToEntity)
				.collect(Collectors.toList());
	}
	
	public Collection<Map<String, Object>> convertDomainEntitiesToDtos(Collection<T> entities) {
		return entities.stream()
				.map(this::convertEntityToDto)
				.collect(Collectors.toList());
	}
}