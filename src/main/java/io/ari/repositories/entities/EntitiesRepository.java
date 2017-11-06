package io.ari.repositories.entities;


import java.util.HashMap;
import java.util.Map;

public class EntitiesRepository<T extends Entity> {

    public T save(T entity) {
        entities.put(entity.getId(), entity);
        return entity;
    }

    public T update(String id, T entity) {
        entities.put(id, entity);
        return entity;
    }

    public T findById(String id) {
        return entities.get(id);
    }

    public boolean exists(String id) {
        return entities.containsKey(id);
    }

    public void deleteById(String entityId) {
        entities.remove(entityId);
    }

    public void deleteAll() {
        entities.clear();
    }

    private Map<String, T> entities = new HashMap<>();
}