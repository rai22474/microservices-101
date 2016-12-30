package stepdefinitions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DefaultFactory {

    public Map<String, Object> createDefaultBundle(List<Map<String, Object>> projections) {

        Map<String, Object> defaultBundle = new HashMap<>();
        Map<String, List<Object>> skeleton = getSkeleton(defaultBundle);
        projections
                .stream()
                .findFirst()
                .get()
                .entrySet()
                .stream()
                .filter(entry ->  Boolean.parseBoolean((String)entry.getValue()))
                .forEach(entry -> addField( entry.getKey(), skeleton.get(entry.getKey())));
        return defaultBundle;
    }

    public void addField(String field, List<Object> entry) {
        if ( entry.get(0) instanceof List) {
            ((List<Object>) entry.get(0)).add(entry.get(1));
        } else {
            ((Map<String, Object>) entry.get(0)).put(field, entry.get(1));
        }
    }

    public abstract Map<String, List<Object>> getSkeleton(Map<String, Object> entity);

}
