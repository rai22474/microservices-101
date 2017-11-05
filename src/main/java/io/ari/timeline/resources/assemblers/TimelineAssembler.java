package io.ari.timeline.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.timeline.domain.TimelineEntry;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TimelineAssembler {

    public Map<String, Object> convertEntitiesToDtos(Collection<TimelineEntry> timelineEntries) {
        Collection<Map<String, Object>> entriesDto = timelineEntries
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

        return ImmutableMap.of("items", entriesDto);
    }

    public Map<String,Object> convertEntityToDto(TimelineEntry timelineEntry) {
        Map<String,Object> entryDto = new HashMap<>();
        entryDto.put("customerId",timelineEntry.getCustomerId());

        return entryDto;
    }
}
