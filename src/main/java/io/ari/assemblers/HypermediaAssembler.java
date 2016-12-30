package io.ari.assemblers;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HypermediaAssembler {

    public Map<String, Object> createLink(String uri, String method, String api) {
        Map<String, Object> link = new HashMap<>();

        link.put(METHOD, method);
        link.put(HREF, uri);
        link.put(API, api);

        return link;
    }

    public Map<String, Object> createSelfLink(String uri, String api) {
        return createLink(uri, GET_METHOD, api);
    }

    public Map<String, Object> createHypermedia(String uri, String api) {
        Map<String, Object> links = new HashMap<>();
        links.put(SELF, createSelfLink(uri, api));
        return links;
    }

    private static final String SELF = "self";

    private static final String GET_METHOD = "GET";

    private static final String API = "api";

    private static final String METHOD = "method";

    private static final String HREF = "href";

}
