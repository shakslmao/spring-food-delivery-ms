package com.devshaks.delivery.customer.kafka;

import com.devshaks.delivery.customer.restaurants.RestaurantDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavouriteEventProducer {
    private final KafkaTemplate<String, RestaurantDTO> kafkaTemplate;

    public void sendFavouriteEvent(RestaurantDTO restaurantDTO) {
        log.info("Sending favourite event to kafka topic: {}", restaurantDTO);
        Message<RestaurantDTO> message = MessageBuilder
                .withPayload(restaurantDTO)
                .setHeader(KafkaHeaders.TOPIC, "favourite-topic")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendFavouriteRemovalEvent(RestaurantDTO restaurantDTO) {
        log.info("Sending favourite removal event to kafka topic: {}", restaurantDTO);
        Message<RestaurantDTO> message = MessageBuilder
                .withPayload(restaurantDTO)
                .setHeader(KafkaHeaders.TOPIC, "favourite-removal-topic")
                .build();
        kafkaTemplate.send(message);
    }
}
