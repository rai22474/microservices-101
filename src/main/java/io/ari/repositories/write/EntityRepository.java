package io.ari.repositories.write;

import io.ari.repositories.exceptions.EntityNotFound;

import java.util.HashMap;
import java.util.Map;

public class EntityRepository extends WriteRepository {

    public EntityRepository(String collectionName) {
        super(collectionName);
    }

    public Map<String, Object> saveEntity(Map<String, Object> entityData) {
        sequential++;
        entityData.put("id",sequential.toString());
        entities.put(sequential.toString(), entityData);

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

    private Integer sequential = 0;

    private Map<String, Map<String, Object>> entities = new HashMap<>();
}