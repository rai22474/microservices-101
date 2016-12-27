package io.ari.assemblers.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.util.Date;

public class DateToISOStringValueConverter extends JsonSerializer<Date> {

	@Override
	public void serialize(Date value, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		if (value == null) {
			return;
		}

		DateTime currentDateTime = new DateTime(value, DateTimeZone.UTC);
		generator.writeString(currentDateTime.toDateTimeISO().toString());
	}
}
