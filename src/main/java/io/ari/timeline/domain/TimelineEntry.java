package io.ari.timeline.domain;


public class TimelineEntry {

    public TimelineEntry(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    private String customerId;

}
