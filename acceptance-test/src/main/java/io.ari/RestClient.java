package io.ari;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class RestClient {

	public RestClient(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	public Response post(String uri, Map<String, Object> data, Map<String, Object> headers) {
		Builder request = createRequestBuilder(uri);
		addHeaders(request, headers);

		return request.post(json(data));
	}

	public Response post(String uri, Map<String, Object> data) {
		return createRequestBuilder(uri)
				.post(json(data));
	}

	public Response post(String uri, Object entity, MediaType contentType) {
		return createRequestBuilder(uri)
				.post(Entity.entity(entity, contentType));
	}

	public Response delete(String uri) {
		return createRequestBuilder(uri).delete();
	}

	public Response delete(String uri, Map<String, Object> headers) {
		Builder request = createRequestBuilder(uri);
		addHeaders(request, headers);

		return request.delete();
	}

	public Response get(String uri, String... queryParams) {
		return createGetRequest(uri, queryParams).get();
	}

	public Response get(String uri, Map<String, Object> headers, String... queryParams) {
		Builder request = createGetRequest(uri, queryParams);
		addHeaders(request, headers);

		return request.get();
	}

	public Response put(String uri, Map<String, Object> data) {
		return put(uri, data, ImmutableMap.of());
	}

	public Response put(String uri, Map<String, Object> data, Map<String, Object> headers) {
		Builder requestBuilder = createRequestBuilder(uri);
		addHeaders(requestBuilder, headers);

		return requestBuilder
				.put(json(data));
	}

	public Builder createRequestBuilder(String uri) {
		return client
				.target(contextRoot)
				.path(uri)
				.request(APPLICATION_JSON);
	}

	private void addHeaders(Builder request, Map<String, Object> headers) {
		headers.forEach(request::header);
	}

	private Builder createGetRequest(String uri, String... queryParams) {
		WebTarget webTarget = client.target(contextRoot).path(uri);

		for (String queryParam : queryParams) {
			String[] values = queryParam.split("=");
			webTarget = webTarget.queryParam(values[0], values[1]);
		}

		return webTarget.request(APPLICATION_JSON);
	}

	private String contextRoot;

	@Autowired
	private Client client;
}

