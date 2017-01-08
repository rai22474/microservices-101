package io.ari.timeline.resources.assemblers;

import io.ari.timeline.domain.TimelineEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class TimelineAssemblerTest {

    @Test
    public void shouldReturnNotNullWhenConvertATimelineEntry() {
        Map<String, Object> timelineEntryDto = timelineAssembler.convertEntityToDto(timelineEntry);

        assertNotNull("The timeline entry dto must be not null", timelineEntryDto);
    }

    @Test
    public void shouldHaveCustomerId() {
        Map<String, Object> timelineEntryDto = timelineAssembler.convertEntityToDto(timelineEntry);

        assertEquals("The timeline entry dto must have customer id", CUSTOMER_ID, timelineEntryDto.get("customerId"));
    }

    private TimelineEntry timelineEntry = new TimelineEntry(CUSTOMER_ID);

    private final static String CUSTOMER_ID = "customerId";

    @InjectMocks
    private TimelineAssembler timelineAssembler;

}
