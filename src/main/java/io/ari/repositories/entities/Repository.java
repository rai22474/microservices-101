package io.ari.repositories.entities;

import io.ari.repositories.exceptions.EntityNotFound;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Repository {

    public Map<String, Object> saveEntity(String id,Map<String, Object> entityData) {
        entities.put(id, entityData);
        return entityData;
    }

    public void delete(String entityId) {
        entities.remove(entityId);
    }

    public Map<String, Object> update(String entityId, Map<String, Object> entityData) {
        return entities.put(entityId, entityData);
    }

    public Map<String, Object> findOne(String entityId) throws EntityNotFound {
        if (!entities.containsKey(entityId)){
            throw new EntityNotFound();
        }

        return entities.get(entityId);
    }

    public Map<String, Map<String, Object>> getEntities(){
        return entities;
    }

    public Optional<Map<String,Object>> findByKey(Map<String, Object> entityKey) {
        return Optional.empty();
    }

    public void deleteAll() {
        entities.clear();
    }

    private Map<String, Map<String, Object>> entities = new HashMap<>();
}