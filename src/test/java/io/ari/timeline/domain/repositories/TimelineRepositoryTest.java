package io.ari.timeline.domain.repositories;

import io.ari.timeline.domain.TimelineEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimelineRepositoryTest {

    @Test
    public void shouldHaveACustomerTimelineNotNull() {
        Collection<TimelineEntry> timeline = timelineRepository.getTimelineByCustomer(CUSTOMER_ID);
        assertNotNull("The timeline must not be null", timeline);
    }

    @Test
    public void shouldHaveOneItemInTheCustomerTimeline() {

        when(timelineEntry.getCustomerId()).thenReturn(CUSTOMER_ID);
        when(timelineSecondEntry.getCustomerId()).thenReturn(SECOND_CUSTOMER_ID);

        timelineRepository.save(timelineEntry);
        timelineRepository.save(timelineSecondEntry);

        Collection<TimelineEntry> timeline = timelineRepository.getTimelineByCustomer(CUSTOMER_ID);

        assertEquals("The timeline length is not the expected ", 1, timeline.size());
    }

    @InjectMocks
    private TimelineRepository timelineRepository;

    @Mock(name = "timelineEntry")
    private TimelineEntry timelineEntry;

    @Mock(name = "timelineSecondEntry")
    private TimelineEntry timelineSecondEntry;

    private static final String CUSTOMER_ID = "customerId";

    private static final String SECOND_CUSTOMER_ID = "customerIdSecond";
}
