package com.karrostech.tool.service;

import com.karrostech.tool.model.AvlEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeofenceEventBuilder extends EventBuilder {

    @Value("${events.avl.geofence.eventid}")
    private int eventId;

    @Value("${events.avl.geofence.data}")
    private String data;

    @Value("${events.avl.geofence.inside.lat}")
    private double geofenceInsideLat;

    @Value("${events.avl.geofence.inside.lon}")
    private double geofenceInsideLon;

    @Value("${events.avl.geofence.outside.lat}")
    private double geofenceOutsideLat;

    @Value("${events.avl.geofence.outside.lon}")
    private double geofenceOutsideLon;

    @Override
    public AvlEvent build() {
        AvlEvent event = buildGenericEvent();

        event.setEventid(eventId);
        event.setAdditionaldata(data);

        return event;
    }

    public AvlEvent buildInside() {
        AvlEvent event = build();
        event.setLatitude(geofenceInsideLat);
        event.setLongitude(geofenceInsideLon);

        return event;
    }

    public AvlEvent buildOutside() {
        AvlEvent event = build();
        event.setLatitude(geofenceOutsideLat);
        event.setLongitude(geofenceOutsideLon);

        return event;
    }


}
