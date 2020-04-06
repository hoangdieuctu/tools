package com.karrostech.tool.service;

import com.karrostech.tool.model.AvlEvent;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public abstract class EventBuilder {

    @Value("${events.avl.busid}")
    private String busId;

    @Value("${events.avl.tenant}")
    private String tenantId;

    @Value("${events.avl.vehicle}")
    private String vehicleId;

    @Value("${events.avl.unitid}")
    private String unitId;

    public abstract AvlEvent build();

    protected AvlEvent buildGenericEvent() {
        AvlEvent event = new AvlEvent();
        event.setId(UUID.randomUUID().toString());
        event.setDatabaseinsertiontime(System.currentTimeMillis());
        event.setEventtime(System.currentTimeMillis());
        event.setGeom("0");
        event.setHeading(0.0);
        event.setLatitude(0.0);
        event.setLongitude(0.0);
        event.setHdop(0.0);
        event.setMileage(0);
        event.setSpeed(0);
        event.setGear("0");

        event.setBusid(busId);
        event.setTenantId(tenantId);
        event.setVehicleId(vehicleId);
        event.setUnitid(unitId);

        return event;
    }
}
