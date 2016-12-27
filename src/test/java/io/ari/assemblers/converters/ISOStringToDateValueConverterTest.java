package io.ari.assemblers.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ISOStringToDateValueConverterTest {


	@Test
	public void shouldTransformTheDateOfTheMovement() throws JsonProcessingException, IOException {
		when(jsonParser.getText()).thenReturn("2012-02-02T11:01:00Z");
		assertEquals("The value of the date is not the expected", gmtTimeZoneExpectedDate(),
				stringToDateConverter.deserialize(jsonParser, serializerContext));
	}

	@Test
	public void shouldDoNothingIfNullTransformTheDateOfTheMovement() throws JsonProcessingException, IOException {
		when(jsonParser.getText()).thenReturn(null);
		assertEquals("The value of the date is not the expected", null,
				stringToDateConverter.deserialize(jsonParser, serializerContext));
	}

	@Test
	public void shouldReturnTheSameValueIfNotADate() throws JsonProcessingException, IOException {
		when(jsonParser.getText()).thenReturn("Invalid date format");
		assertEquals("The value of the date is not the expected", "Invalid date format",
				stringToDateConverter.deserialize(jsonParser, serializerContext));
	}

	private Date gmtTimeZoneExpectedDate() {
		GregorianCalendar calendar = new GregorianCalendar(2012, 1, 2, 11, 01);
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
		return calendar.getTime();
	}

	@InjectMocks
	private ISOStringToDateValueConverter stringToDateConverter;

	@Mock
	private JsonParser jsonParser;

	@Mock
	private DeserializationContext serializerContext;

}
