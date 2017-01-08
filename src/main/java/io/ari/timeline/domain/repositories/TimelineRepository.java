package io.ari.timeline.domain.repositories;

import io.ari.timeline.domain.TimelineEntry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class TimelineRepository {

    public Collection<TimelineEntry> getTimelineByCustomer(String customerId) {
        if (customersTimeline.get(customerId) == null){
            return new ArrayList<>();
        }

        return customersTimeline.get(customerId);
    }

    public TimelineEntry save(TimelineEntry timelineEntry) {

        if (customersTimeline.containsKey(timelineEntry.getCustomerId())){
            customersTimeline.get(timelineEntry.getCustomerId()).add(timelineEntry);
        } else{
            Collection<TimelineEntry> entries = new ArrayList<>();
            entries.add(timelineEntry);

            customersTimeline.put(timelineEntry.getCustomerId(),entries);
        }

        return null;
    }

    private Map<String,Collection<TimelineEntry>> customersTimeline = new HashMap<>();

}
