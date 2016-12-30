package io.ari.repositories.entities;


import io.ari.repositories.assemblers.StorageAssembler;
import io.ari.repositories.exceptions.EntityNotFound;

import java.util.Map;

public class EntitiesRepository<T> extends Repository {

	public EntitiesRepository(StorageAssembler<T> storageAssembler) {
		this.storageAssembler = storageAssembler;
	}

	public T save(T entity) {
		Map<String, Object> entityData = storageAssembler.convertEntityToDto(entity);
		Map<String, Object> savedEntityData = super.saveEntity(((Entity)entity).getId(),entityData);
		return storageAssembler.convertDtoToEntity(savedEntityData);
	}

	public T update(String id, T entity) {
		Map<String, Object> updatedBundleData = super.update(id,
				storageAssembler.convertEntityToDto(entity));
		return convertDtoToEntity(updatedBundleData);
	}

	public T findById(String id) throws EntityNotFound {
		return convertDtoToEntity(super.findOne(id));
	}

	public boolean exists(String id) {
		try {
			super.findOne(id);
			return true;
		} catch (EntityNotFound entityNotFound) {
			return false;
		}
	}

	public void deleteById(String entityId) {
		super.delete(entityId);
	}

	public void deleteAll() {
		super.deleteAll();
	}

	protected T convertDtoToEntity(Map<String, Object> updatedEntityData) {
		return storageAssembler.convertDtoToEntity(updatedEntityData);
	}

	private StorageAssembler<T> storageAssembler;


}