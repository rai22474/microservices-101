package io.ari.moneyRequests.domain.services;

import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.moneyRequests.domain.events.MoneyRequestBundleCreationEventFactory;
import io.ari.moneyRequests.domain.repositories.MoneyRequestBundlesRepository;
import io.ari.timeline.domain.TimelineEntry;
import io.ari.timeline.domain.repositories.TimelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoneyRequestBundleAppService {

    public MoneyRequestBundle process(String customerId, MoneyRequestBundle moneyRequestBundle) {
        MoneyRequestBundle savedMoneyRequestBundle = repository.save(moneyRequestBundle);

        if (moneyRequestBundle.getSourceCommand() == null) {
            timelineRepository.save(createMoneyReceptionTimelineEntry(customerId,moneyRequestBundle));
        }

        savedMoneyRequestBundle.submit();

        return savedMoneyRequestBundle;
    }

    private TimelineEntry createMoneyReceptionTimelineEntry(String customerId,
                                                            MoneyRequestBundle moneyRequestBundle) {
        return new TimelineEntry(customerId);
    }

    @Autowired
    private MoneyRequestBundlesRepository repository;

    @Autowired
    private MoneyRequestBundleCreationEventFactory factory;

    @Autowired
    private TimelineRepository timelineRepository;
}
