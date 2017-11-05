package io.ari.schemaValidations;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.fge.jackson.JsonLoader.fromResource;
import static com.github.fge.jsonschema.main.JsonSchemaFactory.byDefault;
import static com.google.common.collect.Maps.newHashMap;

@Component
public class JsonValidatorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest requestContext, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) requestContext);
        requestContext = wrappedRequest;

        if (requestContext.getContentLength() == -1) {
            chain.doFilter(requestContext, response);
            return;
        }

        String path = ((HttpServletRequest) requestContext).getRequestURI().substring(1);
        String method = ((HttpServletRequest) requestContext).getMethod();

        Map<String, Object> requestPayload = getRequestPayload(new HttpServletRequestWrapper((HttpServletRequest) requestContext));

        Optional<ProcessingReport> report = getJsonSchema(path, method)
                .map(jsonSchema -> getValidationReport(jsonSchema, requestPayload));

        if (report.isPresent()) {
            if (!report.get().isSuccess()) {
                ((HttpServletResponse) response)
                        .setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(validationReportToJson(report.get()));
                return;
            }
        }

        chain.doFilter(requestContext, response);
    }

    private Map<String, Object> validationReportToJson(ProcessingReport report) {
        HashMap<String, Object> errorMap = newHashMap();
        errorMap.put("error", report.toString());
        return errorMap;
    }

    private ProcessingReport getValidationReport(JsonSchema schema, Map<String, Object> payload) {
        try {
            return schema.validate(objectMapper.valueToTree(payload));
        } catch (ProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Optional<JsonSchema> getJsonSchema(String path, String method) {
        Optional<String> schemaLocation = schemasContainer.getSchemaLocation(path, method);
        return schemaLocation.map(this::buildJsonSchema);
    }

    private JsonSchema buildJsonSchema(String location) {
        try {
            return byDefault().getJsonSchema(fromResource(location));
        } catch (ProcessingException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, Object> getRequestPayload(ServletRequest containerRequestContext) {
        try {
            byte[] payloadBytes = IOUtils.toByteArray(containerRequestContext.getInputStream());
            Map<String, Object> payload = objectMapper.readValue(payloadBytes, Map.class);

            return payload;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SchemasContainer schemasContainer;

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}

class RequestWrapper extends HttpServletRequestWrapper {

    private String _body;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        _body = "";
        BufferedReader bufferedReader = request.getReader();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            _body += line;
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(_body.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}