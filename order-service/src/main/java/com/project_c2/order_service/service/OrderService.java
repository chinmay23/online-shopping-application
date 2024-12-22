package com.project_c2.order_service.service;

import com.project_c2.order_service.client.InventoryClient;
import com.project_c2.order_service.order.event.OrderPlacedEvent;
import com.project_c2.order_service.model.Order;
import com.project_c2.order_service.repository.OrderRepository;
import com.project_c2.order_service.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        var isProductInStock=inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if(isProductInStock){
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price().multiply(BigDecimal.valueOf(orderRequest.quantity())));
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);

            //Send message to kafka topic
            OrderPlacedEvent orderPlacedEvent=new OrderPlacedEvent();//(order.getOrderNumber(),orderRequest.userDetails().email());
            orderPlacedEvent.setOrderNumber(order.getOrderNumber());
            orderPlacedEvent.setEmail(orderRequest.userDetails().email());
            orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
            orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());

            log.info("Start - Sending OrderPlacedEvent {} to Kafka topic order-placed",orderPlacedEvent);

            kafkaTemplate.send("order-placed",orderPlacedEvent);
            log.info("End - Sending OrderPlacedEvent {} to Kafka topic order-placed",orderPlacedEvent);

        }else{
            throw new RuntimeException("Product with skuCode " + orderRequest.skuCode() + " with quantity " + orderRequest.quantity() + " is not in stock with the status " + isProductInStock );
        }


    }
}
