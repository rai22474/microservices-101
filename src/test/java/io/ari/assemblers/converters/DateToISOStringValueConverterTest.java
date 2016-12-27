package io.ari.assemblers.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class DateToISOStringValueConverterTest {

	@InjectMocks
	private DateToISOStringValueConverter dateToISOStringValueConverter;

	@Mock
	private JsonGenerator generator;

	@Mock
	private SerializerProvider provider;

	@Test
	public void shouldConvertTheDateInISOString() throws JsonProcessingException, IOException {
		GregorianCalendar calendar = new GregorianCalendar(2012, 1, 2, 11, 01);
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));

		dateToISOStringValueConverter.serialize(calendar.getTime(), generator, provider);

		verify(generator).writeString("2012-02-02T11:01:00.000Z");
	}

	@Test
	public void shouldNotConvertANullValue() throws JsonProcessingException, IOException {

		dateToISOStringValueConverter.serialize(null, generator, provider);
		verifyZeroInteractions(generator);
	}
}
