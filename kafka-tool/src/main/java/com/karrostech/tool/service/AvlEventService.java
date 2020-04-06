package com.karrostech.tool.service;

import com.karrostech.tool.model.AvlEvent;
import com.karrostech.tool.model.Environment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AvlEventService {

    @Autowired
    private RidershipEventBuilder ridershipEventBuilder;

    @Autowired
    private GeofenceEventBuilder geofenceEventBuilder;

    @Autowired
    private KafkaService kafkaService;

    @Value("${events.avl.topic}")
    private String avlEventTopic;

    public void sendAvlEvents() {
        sendRidershipEvent();
        sendGeofenceEvents();
    }

    private void sendRidershipEvent() {
        AvlEvent event = ridershipEventBuilder.build();
        String message = event.toJson();

        log.info("Send ridership event: {}", message);
        kafkaService.send(Environment.NON_DEV, avlEventTopic, message);
    }

    private void sendGeofenceEvents() {
        AvlEvent insideEvent = geofenceEventBuilder.buildInside();
        String insideMsg = insideEvent.toJson();
        log.info("Send geofence inside event: {}", insideMsg);
        kafkaService.send(Environment.NON_DEV, avlEventTopic, insideMsg);

        // avoid the outside event be processed before inside event
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        AvlEvent outsideEvent = geofenceEventBuilder.buildOutside();
        String outsideMsg = outsideEvent.toJson();
        log.info("Send geofence outside event: {}", outsideMsg);
        kafkaService.send(Environment.NON_DEV, avlEventTopic, outsideMsg);
    }
}
