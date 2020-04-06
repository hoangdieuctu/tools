package com.karrostech.tool.service;

import com.karrostech.tool.model.AvlEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RidershipEventBuilder extends EventBuilder {


    @Value("${events.avl.ridership.eventid}")
    private int eventId;

    @Value("${events.avl.ridership.data}")
    private String data;

    @Value("${events.avl.ridership.lat}")
    private double ridershipLat;

    @Value("${events.avl.ridership.lon}")
    private double ridershipLon;

    @Override
    public AvlEvent build() {
        AvlEvent event = buildGenericEvent();

        event.setEventid(eventId);
        event.setAdditionaldata(data);

        event.setLatitude(ridershipLat);
        event.setLongitude(ridershipLon);

        return event;
    }

}
