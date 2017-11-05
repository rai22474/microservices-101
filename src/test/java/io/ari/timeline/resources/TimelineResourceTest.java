package io.ari.timeline.resources;


import com.google.common.collect.ImmutableMap;
import io.ari.customers.domain.exceptions.CustomerExists;
import io.ari.timeline.domain.TimelineEntry;
import io.ari.timeline.domain.repositories.TimelineRepository;
import io.ari.timeline.resources.assemblers.TimelineAssembler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimelineResourceTest {

    @Test
    public void shouldReturnTheOkResponse() throws CustomerExists {
        ResponseEntity response = timelineResource.getCustomerTimeline(CUSTOMER_ID);

        assertEquals("The response code should be OK", 200, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnTheListOfCustomerEntries() {
        Map<String, Object> expectedMovements = createExpectedDtos();
        Collection<TimelineEntry> expectedEntries = createTimelineEntries();

        when(timelineRepository.getTimelineByCustomer(CUSTOMER_ID)).thenReturn(expectedEntries);
        when(timelineAssembler.convertEntitiesToDtos(expectedEntries)).thenReturn(expectedMovements);
        ResponseEntity response = timelineResource.getCustomerTimeline(CUSTOMER_ID);

        Map<String, Object> entries = (Map<String, Object>) response.getBody();

        assertNotNull("The list of movement must be not null", entries);
        assertEquals("The list of movement is the expected", expectedMovements, entries);
    }

    private Collection<TimelineEntry> createTimelineEntries() {
        Collection<TimelineEntry> entries = new ArrayList<>();
        entries.add(new TimelineEntry(CUSTOMER_ID));
        return entries;
    }

    private Map<String,Object> createExpectedDtos(){
        return ImmutableMap.of("items",new ArrayList<>());
    }

    @InjectMocks
    private TimelineResource timelineResource;

    @Mock
    private TimelineRepository timelineRepository;

    @Mock
    private TimelineAssembler timelineAssembler;

    @Mock
    private TimelineEntry timelineEntry;

    private static final String CUSTOMER_ID = "customerId";
}
