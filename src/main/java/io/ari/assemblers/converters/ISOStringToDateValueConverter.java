package io.ari.assemblers.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;

public class ISOStringToDateValueConverter extends JsonDeserializer {

	private static final String ISO_DATE_REGEX = "^(\\d{4}\\-\\d\\d\\-\\d\\d([tT][\\d:\\.]*)?)([zZ]|([+\\-])(\\d\\d):?(\\d\\d))?$";

	@Override
	public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		String value = parser.getText();

		if (value != null && value.matches(ISO_DATE_REGEX)) {
			return new DateTime(value).toDateTime(DateTimeZone.UTC).toDate();
		}
		
		return value;
	}

}
