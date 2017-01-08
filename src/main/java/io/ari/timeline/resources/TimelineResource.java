package io.ari.timeline.resources;

import io.ari.timeline.domain.TimelineEntry;
import io.ari.timeline.domain.repositories.TimelineRepository;
import io.ari.timeline.resources.assemblers.TimelineAssembler;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("timeline")
public class TimelineResource {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getCustomerTimeline(@RequestHeader("x-customer-id") @NotEmpty String customerId) {
        Collection<TimelineEntry> entries = timelineRepository.getTimelineByCustomer(customerId);

        return ResponseEntity.ok().body(timelineAssembler.convertEntitiesToDtos(entries));
    }

    @Autowired
    private TimelineAssembler timelineAssembler;

    @Autowired
    private TimelineRepository timelineRepository;
}
