package io.ari.schema;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import org.junit.Test;

import java.io.IOException;

import static com.github.fge.jackson.JsonLoader.fromResource;
import static com.github.fge.jsonschema.main.JsonSchemaFactory.byDefault;
import static org.junit.Assert.assertEquals;

public abstract class SchemaTest {

    @Test
    public void shouldGetExpectedValidationAgainstSchema() throws IOException, ProcessingException {
        JsonSchema schema = byDefault().getJsonSchema(fromResource("/schemas/" + getSchemaPath()));

        ProcessingReport validationReport = schema.validate(fromResource(getBasePath() + candidatePath));

        assertEquals("Validation report must be the expected.", expectedValidationResult, validationReport.isSuccess());
    }

    protected abstract String getSchemaPath();

    protected abstract String getBasePath();

    public SchemaTest(String candidatePath, boolean expectedValidationResult) {
        this.candidatePath = candidatePath;
        this.expectedValidationResult = expectedValidationResult;
    }

    private String candidatePath;

    private boolean expectedValidationResult;
}
